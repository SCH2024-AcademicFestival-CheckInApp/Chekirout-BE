package com.sch.chekirout.program.application;

import com.sch.chekirout.participation.domain.ParticipationRecord;
import com.sch.chekirout.participation.domain.repository.ParticipationRecordRepository;
import com.sch.chekirout.program.application.dto.request.ProgramParticipationRequest;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.program.domain.repository.ProgramRepository;
import com.sch.chekirout.program.exception.AlreadyParticipatedException;
import com.sch.chekirout.program.exception.DistanceOutOfRangeException;
import com.sch.chekirout.program.exception.ProgramNotFoundException;
import com.sch.chekirout.program.exception.ProgramTimeWindowException;
import com.sch.chekirout.stampCard.application.StampCardService;
import com.sch.chekirout.stampCard.domain.StampCard;
import com.sch.chekirout.stampCard.exception.StampCardNotFoundException;
import com.sch.chekirout.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProgramParticipationService {

    private final ProgramRepository programRepository;
    private final ParticipationRecordRepository participationRecordRepository;
    private final CategoryService categoryService;
    private final StampCardService stampCardService;

    // 기준 좌표 (예시: 36.76836177815623, 126.92743693790044)
    private static final double TARGET_LATITUDE = 36.76836177815623;
    private static final double TARGET_LONGITUDE = 126.92743693790044;
    private static final double MAX_DISTANCE_METERS = 110.0;  // 110m 이내
    private static final int START_TIME_WINDOW_MINUTES = 10;  // 프로그램 시작 10분 전
    private static final int END_TIME_WINDOW_MINUTES = 10;    // 프로그램 종료 10분 후

    @Transactional
    public void participateInProgram(User user, String programId, ProgramParticipationRequest request) {

        Program program = getValidProgram(programId);

        validateAlreadyParticipated(user.getId(), programId, program.getName());

        validateParticipationTime(program, request.getTimestamp());
        validateParticipationLocation(request.getLatitude(), request.getLongitude());

        recordParticipation(user, programId, request);

        // 스탬프 카드가 없는 경우, 새 스탬프 카드 생성
        StampCard stampCard = getOrCreateStampCard(user);

        // 스탬프 카드에 해당 카테고리의 스탬프가 없는 경우, 새 스탬프를 추가하여 기록
        stampCardService.addStampIfNotExists(stampCard, program);
    }

    /**
     * 프로그램 ID로 프로그램을 조회하고, 존재하지 않는 경우 예외를 발생
     * @param programId
     * @return Program  - 조회된 프로그램
     */
    private Program getValidProgram(String programId) {
        return programRepository.findByIdAndDeletedAtIsNull(programId)
                .orElseThrow(() -> new ProgramNotFoundException(programId));
    }

    /**
     * 이미 참여한 프로그램인지 확인
     * @param userId
     * @param programId
     * @param programName
     */
    private void validateAlreadyParticipated(Long userId, String programId, String programName) {
        Optional<ParticipationRecord> participationRecord = participationRecordRepository.findByUserIdAndProgramId(userId, programId);
        if (participationRecord.isPresent()) {
            throw new AlreadyParticipatedException(programName);
        }
    }

    /**
     * 참여 시간이 프로그램 참여 인정 시간 범위 내에 있는지 확인
     * @param program
     * @param participationTime
     */
    private void validateParticipationTime(Program program, LocalDateTime participationTime) {
        LocalDateTime startWindow = program.getStartTimestamp().minusMinutes(START_TIME_WINDOW_MINUTES);
        LocalDateTime endWindow = program.getEndTimestamp().plusMinutes(END_TIME_WINDOW_MINUTES);

        if (participationTime.isBefore(startWindow) || participationTime.isAfter(endWindow)) {
            throw new ProgramTimeWindowException();
        }
    }

    /**
     * 참여 위치가 기준 좌표에서 허용 범위 내에 있는지 확인
     * @param latitude
     * @param longitude
     */
    private void validateParticipationLocation(double latitude, double longitude) {
        if (!isWithinRange(latitude, longitude, TARGET_LATITUDE, TARGET_LONGITUDE, MAX_DISTANCE_METERS)) {
            throw new DistanceOutOfRangeException();
        }
    }

    /**
     * 참여 기록 저장
     * @param user
     * @param programId
     * @param request
     */
    private void recordParticipation(User user, String programId, ProgramParticipationRequest request) {
        ParticipationRecord participationRecord = ParticipationRecord.builder()
                .userId(user.getId())
                .programId(programId)
                .participationTime(request.getTimestamp())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        participationRecordRepository.save(participationRecord);
    }

    /**
     * 사용자의 스탬프 카드를 조회하거나, 없는 경우 새로 생성
     * @param user
     * @return StampCard  - 사용자의 스탬프 카드
     */
    private StampCard getOrCreateStampCard(User user) {
        if (!stampCardService.existsStampCard(user.getId())) {
            stampCardService.createStampCard(user.getId());
        }
        return stampCardService.getStampCard(user.getId());
    }


    /**
     * 구면 거리 계산 (Haversine 공식)
     * 참고: https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi
     * @param latitude - 현재 위치의 위도
     * @param longitude- 현재 위치의 경도
     * @param targetLatitude- 기준 좌표의 위도
     * @param targetLongitude- 기준 좌표의 경도
     * @param maxDistanceMeters
     *
     * @return boolean  - 거리가 허용 범위 내에 있는지 여부
     */
    private boolean isWithinRange(double latitude, double longitude, double targetLatitude, double targetLongitude, double maxDistanceMeters) {
        final int EARTH_RADIUS = 6371; // 지구 반지름 (킬로미터)

        double latDistance = Math.toRadians(targetLatitude - latitude);
        double lonDistance = Math.toRadians(targetLongitude - longitude);

        // 위도 및 경도의 차이에 대한 Haversine 공식 계산
        double haversineLat = Math.sin(latDistance / 2) * Math.sin(latDistance / 2);
        double haversineLon = Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(targetLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        // Haversine 공식에서 구하는 중심 각
        double centralAngle = 2 * Math.atan2(Math.sqrt(haversineLat + haversineLon), Math.sqrt(1 - (haversineLat + haversineLon)));

        // 중심 각에 따른 거리 계산
        double distanceInMeters = EARTH_RADIUS * centralAngle * 1000; // 미터로 변환

        return distanceInMeters <= maxDistanceMeters;
    }
}

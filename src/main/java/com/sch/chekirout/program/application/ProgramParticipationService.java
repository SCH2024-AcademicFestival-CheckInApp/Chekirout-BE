package com.sch.chekirout.program.application;

import com.sch.chekirout.program.application.dto.request.ProgramParticipationRequest;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.program.domain.repository.ProgramRepository;
import com.sch.chekirout.program.exception.DistanceOutOfRangeException;
import com.sch.chekirout.program.exception.ProgramNotFoundException;
import com.sch.chekirout.program.exception.ProgramTimeWindowException;
import com.sch.chekirout.stampCard.application.StampCardService;
import com.sch.chekirout.stampCard.domain.Stamp;
import com.sch.chekirout.stampCard.domain.StampCard;
import com.sch.chekirout.stampCard.domain.repository.StampCardRepository;
import com.sch.chekirout.stampCard.exception.StampCardNotFoundException;
import com.sch.chekirout.user.domain.Repository.UserRepository;
import com.sch.chekirout.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sch.chekirout.participation.domain.ParticipationRecord;
import com.sch.chekirout.participation.domain.repository.ParticipationRecordRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ProgramParticipationService {

    private final UserRepository userRepository;
    private final ProgramRepository programRepository;
    private final ParticipationRecordRepository participationRecordRepository;
    private final CategoryService categoryService;

    // 기준 좌표 (예시: 36.76836177815623, 126.92743693790044)
    private static final double TARGET_LATITUDE = 36.76836177815623;
    private static final double TARGET_LONGITUDE = 126.92743693790044;
    private static final double MAX_DISTANCE_METERS = 110.0;  // 110m 이내
    private final StampCardService stampCardService;

    @Transactional
    public void participateInProgram(User user, String programId, ProgramParticipationRequest request) {

        Program program = programRepository.findByIdAndDeletedAtIsNull(programId)
                .orElseThrow(() -> new ProgramNotFoundException(programId));

        LocalDateTime startWindow = program.getStartTimestamp().minusMinutes(10);
        LocalDateTime endWindow = program.getEndTimestamp().plusMinutes(10);

        if(request.getTimestamp().isBefore(startWindow) || request.getTimestamp().isAfter(endWindow)) {
            throw new ProgramTimeWindowException();
        }

        if(!isWithinRange(request.getLatitude(), request.getLongitude(), TARGET_LATITUDE, TARGET_LONGITUDE, MAX_DISTANCE_METERS)) {
            throw new DistanceOutOfRangeException();
        }

        ParticipationRecord participationRecord = ParticipationRecord.builder()
                .userId(user.getId())
                .programId(programId)
                .participationTime(request.getTimestamp())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        participationRecordRepository.save(participationRecord);

        // 스탬프 카드가 없는 경우, 새 스탬프 카드 생성
        StampCard stampCard;
        try {
            stampCard = stampCardService.getStampCard(user.getId());
        } catch (StampCardNotFoundException e) {
            stampCardService.createStampCard(user.getId());
            stampCard = stampCardService.getStampCard(user.getId());
        }

        // 스탬프 카드에 해당 카테고리의 스탬프가 없는 경우, 새 스탬프를 추가하여 기록
        stampCardService.addStampIfNotExists(stampCard, program);
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
     * @return
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

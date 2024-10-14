package com.sch.chekirout.stampCard.application;

import com.sch.chekirout.program.application.CategoryService;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.stampCard.application.dto.response.StampCardDetail;
import com.sch.chekirout.stampCard.application.dto.response.StampCardResponse;
import com.sch.chekirout.stampCard.domain.Stamp;
import com.sch.chekirout.stampCard.domain.StampCard;
import com.sch.chekirout.stampCard.domain.repository.StampCardRepository;
import com.sch.chekirout.stampCard.exception.StampCardNotFoundException;
import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StampCardService {

    private final StampCardRepository stampCardRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    /**
     * 스탬프카드 생성
     * @param userId
     */
    @Transactional
    public void createStampCard(Long userId) {
        StampCard stampCard = StampCard.createNewStampCard(userId);
        stampCardRepository.save(stampCard);
    }

    /**
     * 경품 추첨 대상자 수 조회
     * 스탬프카드가 있는 모든 사용자를 추첨 대상으로 함
     * @return 경품 추첨 대상자 수
     */
    @Transactional(readOnly = true)
    public Long getEligibleForPrizeCount() {
        return stampCardRepository.countAllBy();
    }

    /**
     * 완료된 스탬프카드 페이징 조회
     * @param pageable
     * @return 완료된 스탬프카드 목록
     */
    @Transactional(readOnly = true)
    public Page<StampCardResponse> getCompletedStampCards(Pageable pageable) {
        Page<StampCard> stampCards = stampCardRepository.findAllByCompletedAtIsNotNull(pageable);

        return stampCards.map(stampCard -> {
            User user = userService.findById(stampCard.getUserId());
            return StampCardResponse.from(stampCard, user);
        });
    }

    /**
     * 완료된 스탬프 카드 개수 조회
     * @return 완료된 경품 수
     */
    @Transactional(readOnly = true)
    public Long getCompletedPrizeCount() {
        return stampCardRepository.countByCompletedAtIsNotNull();
    }

    /**
     * 스탬프카드 페이징 조회
     * @param pageable
     * @return 스탬프카드 목록
     */
    @Transactional(readOnly = true)
    public Page<StampCardResponse> getStampCardList(Pageable pageable) {
        Page<StampCard> stampCards = stampCardRepository.findAllOrderByStampsSizeAndCompletedAt(pageable);

        return stampCards.map(stampCard -> {
            User user = userService.findById(stampCard.getUserId());  // Get User by userId
            return StampCardResponse.from(stampCard, user);
        });
    }

    /**
     * 모든 스탬프카드 조회
     * @return 스탬프카드 목록
     */
    @Transactional(readOnly = true)
    public List<StampCard> getAllStampCard() {
        return stampCardRepository.findAll();
    }

    /**
     * 로그인한 사용자의 스탬프카드 조회
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public StampCardDetail getStampCardDetail(User user) {
        return StampCardDetail.from(getStampCard(user), user);
    }

    /**
     * 학번으로 스탬프카드 조회
     * @param studentId
     * @return
     */
    @Transactional(readOnly = true)
    public StampCardDetail getStampCardDetailByStduentId(String studentId) {
        User user = userService.findUserByUsername(studentId);
        return StampCardDetail.from(getStampCardByStudentId(user), user);
    }

    /**
     * 스탬프카드 존재 여부 확인
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public boolean existsStampCard(Long userId) {
        return stampCardRepository.existsByUserId(userId);
    }

    @Transactional
    public void addStampIfNotExists(StampCard stampCard, Program program, LocalDateTime timestamp) {
        // 카테고리 ID 가져오기
        Long categoryId = program.getCategory().getId();

        // 스탬프 카드에 해당 카테고리의 스탬프가 없는 경우에만 스탬프 추가
        if (!stampCard.hasStampForCategory(categoryId)) {
            Stamp newStamp = Stamp.builder()
                    .categoryId(categoryId)
                    .programId(program.getId())
                    .categoryName(program.getCategory().getName())
                    .programName(program.getName())
                    .timestamp(timestamp)
                    .build();

            // 카테고리 검증 및 스탬프 추가
            stampCard.addStamp(newStamp, categoryService.getValidCategoryIds());
            stampCardRepository.save(stampCard);
        }
    }

    public StampCard getStampCard(User user) {
        return stampCardRepository.findByUserId(user.getId())
                .orElseThrow(() -> new StampCardNotFoundException(user.getUsername()));
    }

    public StampCard getStampCardByStudentId(User user) {
        return stampCardRepository.findByUserId(user.getId())
                .orElseThrow(() -> new StampCardNotFoundException(user.getUsername()));
    }

    public List<StampCard> findCompletedAndNotClaimedStampCards() {
        return stampCardRepository.findAllByExclusivePrizeClaimedAtIsNull();
    }

    public List<StampCard> findCompletedStampCards() {
        return stampCardRepository.findAll();
    }
}

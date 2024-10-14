package com.sch.chekirout.prizeDraw.application;

import com.sch.chekirout.prizeDraw.application.dto.response.DrawResult;
import com.sch.chekirout.prizeDraw.application.dto.response.PrizeWinnerResponse;
import com.sch.chekirout.prizeDraw.application.dto.response.WinnerResponse;
import com.sch.chekirout.prizeDraw.domain.Prize;
import com.sch.chekirout.prizeDraw.domain.PrizeClaimType;
import com.sch.chekirout.prizeDraw.domain.PrizeWinner;
import com.sch.chekirout.prizeDraw.domain.repository.PrizeRepository;
import com.sch.chekirout.prizeDraw.domain.repository.PrizeWinnerRepository;
import com.sch.chekirout.prizeDraw.exception.AlreadyClaimedException;
import com.sch.chekirout.prizeDraw.exception.PrizeNotFoundException;
import com.sch.chekirout.prizeDraw.exception.PrizeWinnerNotFoundException;
import com.sch.chekirout.stampCard.application.StampCardService;
import com.sch.chekirout.stampCard.domain.StampCard;
import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrizeDrawService {

    private final PrizeWinnerRepository prizeWinnerRepository;
    private final PrizeRepository prizeRepository;
    private final StampCardService stampCardService;
    private final UserService userService;

    @Transactional
    public DrawResult drawPrizeWinners(Long prizeId, Integer numberOfWinners) {
        Prize prize = prizeRepository.findById(prizeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid prize ID"));

        List<StampCard> eligibleStampCards;

        // 경품 중복 수령 가능 여부에 따라 가져올 스탬프 카드 결정
        if (prize.getPrizeClaimType() == PrizeClaimType.SINGLE_CLAIM) {
            // 중복 수령 불가능한 경우 exclusivePrizeClaimedAt이 null인 스탬프 카드만 가져오기
            eligibleStampCards = stampCardService.findCompletedAndNotClaimedStampCards();
        } else {
            // 중복 수령 가능한 경우 완료된 모든 스탬프 카드 가져오기
            eligibleStampCards = stampCardService.findCompletedStampCards();
        }

        if (eligibleStampCards.size() < numberOfWinners) {
            throw new IllegalArgumentException("Not enough eligible participants for the draw");
        }

        // Fisher-Yates 알고리즘을 사용해 무작위로 numberOfWinners명만 추첨
        List<StampCard> selectedWinners = selectRandomWinners(eligibleStampCards, numberOfWinners);

        // WinnerResponse 목록을 생성
        List<WinnerResponse> winners = new ArrayList<>();

        // 경품 중복 가능 여부에 따른 처리
        for (StampCard stampCard : selectedWinners) {
            if (prize.getPrizeClaimType() == PrizeClaimType.SINGLE_CLAIM) {
                stampCard.updateExclusivePrizeClaimedAt();
            }

            // PrizeWinner 생성 및 저장
            PrizeWinner prizeWinner = PrizeWinner.builder()
                    .userId(stampCard.getUserId())
                    .stampCardId(stampCard.getId())
                    .prizeId(prizeId)
                    .prizeDrawnAt(LocalDateTime.now())
                    .build();

            prizeWinnerRepository.save(prizeWinner);

            User user = userService.findById(stampCard.getUserId());
            winners.add(WinnerResponse.from(user, prize));
        }

        return new DrawResult(prizeId, prize.getPrizeName(), numberOfWinners, winners);
    }

    private List<StampCard> selectRandomWinners(List<StampCard> eligibleStampCards, int numberOfWinners) {
        Random random = new Random();

        // Fisher-Yates 알고리즘을 사용해 리스트의 일부만 섞기
        for (int i = eligibleStampCards.size() - 1; i >= eligibleStampCards.size() - numberOfWinners; i--) {
            int j = random.nextInt(i + 1); // 0부터 i까지 무작위 인덱스 선택
            Collections.swap(eligibleStampCards, i, j); // i와 j를 교환
        }

        // 마지막 numberOfWinners개의 요소 반환
        return eligibleStampCards.subList(eligibleStampCards.size() - numberOfWinners, eligibleStampCards.size());
    }

    @Transactional
    public void confirmPrizeClaim(String prizeWinnerId) {
        User user = userService.findUserByUsername(prizeWinnerId);

        PrizeWinner prizeWinner = prizeWinnerRepository.findById(user.getId())
                .orElseThrow(() -> new PrizeWinnerNotFoundException(user.getName()));

        if (prizeWinner.getPrizeClaimedAt() != null) {
            throw new AlreadyClaimedException();
        }

        prizeWinner.updatePrizeClaimedAt();
    }

    // 전체 경품 당첨자 목록 조회
    @Transactional(readOnly = true)
    public List<PrizeWinnerResponse> getAllPrizeWinners() {
        return prizeWinnerRepository.findAll().stream()
                .map(this::mapToPrizeWinnerResponse) // PrizeWinner를 PrizeWinnerResponse로 변환
                .collect(Collectors.toList());
    }

    // 경품 별 당첨자 목록 조회
    @Transactional(readOnly = true)
    public List<PrizeWinnerResponse> getPrizeWinners(Long prizeId) {
        return prizeWinnerRepository.findAllByPrizeId(prizeId).stream()
                .map(this::mapToPrizeWinnerResponse) // PrizeWinner를 PrizeWinnerResponse로 변환
                .collect(Collectors.toList());
    }

    // PrizeWinner를 PrizeWinnerResponse로 매핑하는 메서드
    private PrizeWinnerResponse mapToPrizeWinnerResponse(PrizeWinner prizeWinner) {
        User user = userService.findById(prizeWinner.getUserId());
        Prize prize = prizeRepository.findById(prizeWinner.getPrizeId())
                .orElseThrow(() -> new PrizeNotFoundException(prizeWinner.getPrizeId()));

        return PrizeWinnerResponse.from(prizeWinner, user, prize);
    }
}

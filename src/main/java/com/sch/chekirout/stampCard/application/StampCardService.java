package com.sch.chekirout.stampCard.application;

import com.sch.chekirout.program.application.CategoryService;
import com.sch.chekirout.program.domain.Program;
import com.sch.chekirout.stampCard.domain.Stamp;
import com.sch.chekirout.stampCard.domain.StampCard;
import com.sch.chekirout.stampCard.domain.repository.StampCardRepository;
import com.sch.chekirout.stampCard.exception.StampCardNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StampCardService {

    private final StampCardRepository stampCardRepository;
    private final CategoryService categoryService;

    @Transactional
    public void createStampCard(Long userId) {
        StampCard stampCard = StampCard.createNewStampCard(userId);
        stampCardRepository.save(stampCard);
    }

    @Transactional(readOnly = true)
    public StampCard getStampCard(Long userId) {
        return stampCardRepository.findByUserId(userId)
                .orElseThrow(() -> new StampCardNotFoundException(userId));
    }

    @Transactional
    public void addStampIfNotExists(StampCard stampCard, Program program) {
        // 카테고리 ID 가져오기
        Long categoryId = program.getCategory().getId();

        // 스탬프 카드에 해당 카테고리의 스탬프가 없는 경우에만 스탬프 추가
        if (!stampCard.hasStampForCategory(categoryId)) {
            Stamp newStamp = Stamp.builder()
                    .categoryId(categoryId)
                    .programId(program.getId())
                    .categoryName(program.getCategory().getName())
                    .programName(program.getName())
                    .timestamp(LocalDateTime.now())
                    .build();

            // 카테고리 검증 및 스탬프 추가
            stampCard.addStamp(newStamp, categoryService.getValidCategoryIds());
            stampCardRepository.save(stampCard);
        }
    }
}

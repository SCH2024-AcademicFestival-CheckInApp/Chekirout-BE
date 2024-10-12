package com.sch.chekirout.prizeDraw.application;

import com.sch.chekirout.prizeDraw.application.dto.request.PrizeRequest;
import com.sch.chekirout.prizeDraw.application.dto.response.PrizeResponse;
import com.sch.chekirout.prizeDraw.domain.Prize;
import com.sch.chekirout.prizeDraw.domain.repository.PrizeRepository;
import com.sch.chekirout.prizeDraw.exception.PrizeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrizeService {

    private final PrizeRepository prizeRepository;

    @Transactional
    public Long savePrize(PrizeRequest prizeRequest) {
        return prizeRepository.save(prizeRequest.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public List<PrizeResponse> getPrizes() {
        return prizeRepository.findAllByDeletedAtIsNull().stream()
                .map(PrizeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PrizeResponse getPrize(Long prizeId) {
        Prize prize =  prizeRepository.findById(prizeId)
                .orElseThrow(() -> new PrizeNotFoundException(prizeId));

        return PrizeResponse.from(prize);
    }

    @Transactional
    public void updatePrize(Long prizeId, PrizeRequest prizeRequest) {
        Prize prize = prizeRepository.findById(prizeId)
                .orElseThrow(() -> new PrizeNotFoundException(prizeId));

        prize.update(prizeRequest);
    }

    @Transactional
    public void deletePrize(Long prizeId) {
        Prize prize = prizeRepository.findById(prizeId)
                .orElseThrow(() -> new PrizeNotFoundException(prizeId));

        prize.delete();
    }
}
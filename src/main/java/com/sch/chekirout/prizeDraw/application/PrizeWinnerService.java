package com.sch.chekirout.prizeDraw.application;

import com.sch.chekirout.prizeDraw.domain.repository.PrizeRepository;
import com.sch.chekirout.prizeDraw.domain.repository.PrizeWinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrizeWinnerService {

    private final PrizeWinnerRepository prizeWinnerRepository;
    private final PrizeRepository prizeRepository;
}

package com.sch.chekirout.prizeDraw.presentation;

import com.sch.chekirout.prizeDraw.application.PrizeDrawService;
import com.sch.chekirout.prizeDraw.application.dto.response.PrizeWinnerResponse;
import com.sch.chekirout.user.application.UserService;
import com.sch.chekirout.user.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/prize-draws")
@Tag(name = "Prize Draw API", description = "User 경품 추첨 당첨 확인 API")
public class PrizeDrawController {

    private final PrizeDrawService prizeDrawService;
    private final UserService userService;


    @GetMapping("/my-wins")
    public ResponseEntity<List<PrizeWinnerResponse>> getEligableForPrizeCount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.findUserByUsername(currentUsername);

        return ResponseEntity.ok(prizeDrawService.getPrizeWinnersByStudentId(user.getUsername()));
    }
}

package com.example.econflip.domain.user.controller.UserCardController;

import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.exception.code.UserSuccessCode;
import com.example.econflip.domain.user.service.UserCardService;
import com.example.econflip.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewPageController implements ReviewPageControllerDocs{
    private final UserCardService userCardService;

    @GetMapping("/api/review")
    public ApiResponse<UserCardResDTO.reviewPage> reviewPage(
            @AuthenticationPrincipal(expression = "user") User user
    ){
        return ApiResponse.onSuccess(
                UserSuccessCode.REVIEW_PAGE_OK,
                userCardService.getReviewPage(user.getId()));
    }

    // 복습 완료 처리
    @PostMapping("/api/review/complete")
    public void completeReview(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        userCardService.completeReview(user.getId());
    }
}

package com.example.econflip.domain.user.controller.UserCardController;

import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.exception.code.UserSuccessCode;
import com.example.econflip.domain.user.service.UserCardService;
import com.example.econflip.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewPageController implements ReviewPageControllerDocs{
    private final UserCardService userCardService;

    @GetMapping("/review")
    public ApiResponse<UserCardResDTO.reviewPage> reviewPage(Long userId){
        UserCardResDTO.reviewPage result = userCardService.getReviewPage(userId);

        UserSuccessCode code = UserSuccessCode.OK;
        return ApiResponse.onSuccess(code, result);
    }
}

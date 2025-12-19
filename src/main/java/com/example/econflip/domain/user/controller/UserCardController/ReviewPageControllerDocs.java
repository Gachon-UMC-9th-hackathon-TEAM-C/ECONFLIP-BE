package com.example.econflip.domain.user.controller.UserCardController;

import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "Review", description = "복습 페이지 API")
public interface ReviewPageControllerDocs {

    // api/review
    @Operation(
            summary = "복습 페이지 조회"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<UserCardResDTO.reviewPage> reviewPage(@AuthenticationPrincipal(expression = "user") User user);
}

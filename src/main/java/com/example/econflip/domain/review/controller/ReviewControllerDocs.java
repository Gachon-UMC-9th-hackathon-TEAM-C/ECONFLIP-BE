package com.example.econflip.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

public interface ReviewControllerDocs {
    @Operation(
            summary = "복습 용어 조회 API",
            description = "복습이 필요한 용어를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    String searchReviews();

    @Operation(
            summary = "용어 복습 등록 API",
            description = "틀린 문제의 용어를 복습에 추가합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    String insertReview(@RequestBody Long cardId);

    @Operation(
            summary = "용어 복습 삭제 API",
            description = "복습 완료한 용어를 복습에서 제거합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    String deleteReview(@RequestBody Long cardId);
}

package com.example.econflip.domain.user.controller.UserController;

import com.example.econflip.domain.user.dto.UserReqDTO;
import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface UserControllerDocs {
    // api/mypage
    @Operation(
            summary = "마이페이지 조회 API",
            description = "유저 마이페이지 화면 데이터를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "마이페이지 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    ApiResponse<UserResDTO.UserMyPage> getMyPage(@AuthenticationPrincipal(expression = "user") User user);

   // api/dailyStudy
    @Operation(
            summary = "사용자 하루 학습량 설정 업데이트 API",
            description = "하루 학습 분량 등 사용자 설정 정보를 수정합니다. 온보딩 과정에서도 사용됩니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 설정 업데이트 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    ApiResponse<Void> updateMyDailyStudy(
            @AuthenticationPrincipal(expression = "user") User user,
            @NotNull @RequestParam Integer count
    );


    // api/home
    @Operation(
            summary = "홈 화면 조회 API",
            description = "로그인한 사용자의 홈 화면에 필요한 데이터를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "홈 화면 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    ApiResponse<UserResDTO.UserHomePage> getHome(
            @AuthenticationPrincipal(expression = "user") User user
    );

    // GET api/mypage/badges
    @Operation(
            summary = "사용자 배지 조회 API",
            description = "로그인한 사용자가 획득한 배지 목록과 상태를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "배지 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    ApiResponse<List<UserResDTO.BadgeStatus>> getUserBadges(
            @AuthenticationPrincipal(expression = "user") User user
    );
    // PATCH api/mypage/badges
    @Operation(
            summary = "마이페이지 배지 구성 변경 API",
            description = "사용자가 선택한 배지들을 마이페이지에 노출시킵니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<Void> selectMyPageBadges(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody UserReqDTO.BadgeSelectReqDTO request
    );
}

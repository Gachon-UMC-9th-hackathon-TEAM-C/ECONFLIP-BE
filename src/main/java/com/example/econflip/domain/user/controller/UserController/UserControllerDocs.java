package com.example.econflip.domain.user.controller.UserController;

import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


public interface UserControllerDocs {
    // api/mypage
    @Operation(
            summary = "마이페이지 조회 API",
            description = "유저 마이페이지 화면 데이터를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<UserResDTO.UserMyPage> getMyPage(Long userId);

    // api/me
    @Operation(
            summary = "사용자 설정 업데이트 API",
            description = "하루 학습 분량 등 사용자 설정 정보를 수정합니다. 온보딩 과정에서도 사용됩니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    UserResDTO.UserSetting updateMySetting();


    // api/home
    @Operation(
            summary = "홈 화면 조회 API",
            description = "홈 화면에 필요한 데이터를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<UserResDTO.UserHomePage> getHome(Long userId);
}

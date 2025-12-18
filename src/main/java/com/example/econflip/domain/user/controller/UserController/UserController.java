package com.example.econflip.domain.user.controller.UserController;

import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.exception.code.UserSuccessCode;
import com.example.econflip.domain.user.service.UserService;
import com.example.econflip.global.apiPayload.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {
    private final UserService userService;

    // 마이페이지 조회
    @Override
    @GetMapping("/mypage")
    public ApiResponse<UserResDTO.UserMyPage> getMyPage(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        Long userId = user.getId();

        return ApiResponse.onSuccess(
                UserSuccessCode.OK,
                userService.getMypage(userId)
        );
    }

    // User 설정 업데이트
    @Override
    @PatchMapping("/me")
    public UserResDTO.UserSetting  updateMySetting(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        return null;
    }

    // 홈 화면 조회
    @Override
    @GetMapping("/home")
    public UserResDTO.UserMyPage getHome(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        return null;
    }
}

package com.example.econflip.global.auth.controller;

import com.example.econflip.global.apiPayload.ApiResponse;
import com.example.econflip.global.auth.exception.code.AuthSuccessCode;
import com.example.econflip.global.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @Override
    @GetMapping("/login/naver")
    public void naverLoginInfo(){}

    @Override
    @GetMapping("/login/kakao")
    public void kakaoLoginInfo(){}

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){
        return authService.reissue(request,response);
    }

    // 로그아웃
    @Override
    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletResponse response) {

        authService.logout(response);
        AuthSuccessCode code = AuthSuccessCode.LOGOUT_SUCCESS;
        return ApiResponse.onSuccess(code,null);
    }
}

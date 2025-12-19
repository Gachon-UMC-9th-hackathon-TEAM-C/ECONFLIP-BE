package com.example.econflip.global.config.security.auth.Controller;

import com.example.econflip.global.config.security.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/naver")
    @Operation(
            summary = "네이버 로그인 (브라우저 리다이렉트)",
            description = """
    이 엔드포인트는 API 호출용이 아닙니다.

    프론트엔드에서 로그인 버튼 클릭 시
    아래 URL로 페이지 이동(redirect)시키면
    네이버 로그인이 시작됩니다.

    [개발 환경]
    http://localhost:8080/oauth2/authorization/naver
    [운영 환경]
    http://52.79.121.228:8080/oauth2/authorization/naver
    """
    )
    public void naverLoginInfo(){}

    @GetMapping("/login/kakao")
    @Operation(
            summary = "카카오 로그인 (브라우저 리다이렉트)",
            description = """
    이 엔드포인트는 API 호출용이 아닙니다.

    프론트엔드에서 로그인 버튼 클릭 시
    아래 URL로 페이지 이동(redirect)시키면
    카카오 로그인이 시작됩니다.

    [개발 환경]
    http://localhost:8080/oauth2/authorization/kakao
    [운영 환경]
    http://52.79.121.228:8080/oauth2/authorization/kakao
    """
    )
    public void kakaoLoginInfo(){}
}

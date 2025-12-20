package com.example.econflip.global.config.security.auth.controller;

import com.example.econflip.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Auth", description = "인증 관련 API")
public interface AuthControllerDocs {

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
    default void naverLoginInfo(){}

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
    default void kakaoLoginInfo(){}

    @Operation(
            summary = "Access Token 재발급",
            description = """
            만료된 Access Token을 Refresh Token을 이용해 재발급합니다.
            
            - Refresh Token은 HttpOnly Cookie로 전달됩니다.
            - 요청 본문이나 Authorization 헤더는 필요하지 않습니다.
            - Refresh Token이 유효하지 않으면 재발급에 실패합니다.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access Token 재발급 성공"),
            @ApiResponse(responseCode = "401", description = "Refresh Token이 없거나 유효하지 않음"),
            @ApiResponse(responseCode = "403", description = "Refresh Token 만료"),
    })
    @PostMapping("/refresh")
    ResponseEntity<?> reissue(
            HttpServletRequest request,
            HttpServletResponse response
    );

    @Operation(
            summary = "로그아웃",
            description = """
            현재 로그인된 사용자를 로그아웃 처리합니다.
            
            - 인증된 사용자만 호출할 수 있습니다.
            - 서버에 저장된 Refresh Token을 삭제합니다.
            - Access Token 및 Refresh Token 쿠키를 만료시킵니다.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
    })
    @PostMapping("/logout")
    ResponseEntity<Void> logout(
            @AuthenticationPrincipal User user,
            HttpServletResponse response
    );
}

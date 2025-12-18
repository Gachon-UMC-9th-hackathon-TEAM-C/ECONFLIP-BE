package com.example.econflip.global.config.security.oauth.Controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/swagger/login")
public class OAuthSwaggerController {

    @GetMapping("/naver")
    @Operation(
            summary = "네이버 로그인 (브라우저 리다이렉트)",
            description = """
        프론트엔드에서 아래 URL로 이동시키면 네이버 로그인이 시작됩니다.
        개발단계라 아직은 로컬...
        👉 http://localhost:8080/oauth2/authorization/naver
        """
    )
    public void naverLoginInfo(){}
}

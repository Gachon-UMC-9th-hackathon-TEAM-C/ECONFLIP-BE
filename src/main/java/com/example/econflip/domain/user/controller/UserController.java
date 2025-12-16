package com.example.econflip.domain.user.controller;

import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{
    private final UserService userService;

    // 마이페이지 조회
    @Override
    @GetMapping("/mypage")
    public UserResDTO.UserMyPage getMyPage() {
        return null;
    }

    // User 설정 업데이트
    @Override
    @PatchMapping("/me")
    public UserResDTO.UserSetting  updateMySetting() {
        return null;
    }

    // 홈 화면 조회
    @Override
    @GetMapping("/home")
    public UserResDTO.UserMyPage getHome() {
        return null;
    }
}

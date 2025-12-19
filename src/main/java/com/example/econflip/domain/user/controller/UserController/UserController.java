package com.example.econflip.domain.user.controller.UserController;

import com.example.econflip.domain.user.dto.UserReqDTO;
import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.exception.code.UserSuccessCode;
import com.example.econflip.domain.user.service.UserService;
import com.example.econflip.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {
    private final UserService userService;

    // 마이페이지 조회
    @Override
    @GetMapping("/api/mypage")
    public ApiResponse<UserResDTO.UserMyPage> getMyPage(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        Long userId = user.getId();

        UserSuccessCode code = UserSuccessCode.MYPAGE_OK;
        return ApiResponse.onSuccess(code, userService.getMypage(userId));
    }

    // User 설정 업데이트
    @Override
    @PatchMapping("/api/dailyStudy")
    public ApiResponse<Void> updateMyDailyStudy(
      @AuthenticationPrincipal(expression = "user") User user, @NotNull @RequestParam Integer count) {

      Long userId = user.getId();
      UserSuccessCode code = UserSuccessCode.DAILY_STUDY_UPDATED;
        userService.updateDailyStudy(userId, count);
        return ApiResponse.onSuccess(code, null);
    }

    // 홈 화면 조회
    @Override
    @GetMapping("/api/home")
    public ApiResponse<UserResDTO.UserHomePage> getHome(
      @AuthenticationPrincipal(expression = "user") User user) {
        Long userId = user.getId();
        UserSuccessCode code = UserSuccessCode.HOMEPAGE_OK;
        return ApiResponse.onSuccess(code, userService.getHomePage(userId));
    }

    @Override
    @GetMapping("/api/mypage/badges")
    public ApiResponse<List<UserResDTO.BadgeStatus>> getUserBadges(
    @AuthenticationPrincipal(expression = "user") User user){
        Long userId = user.getId();
        UserSuccessCode code = UserSuccessCode.BADGES_OK;
        return ApiResponse.onSuccess(code, userService.getUserBadges(userId));
    }

    @Override
    @PatchMapping("/api/mypage/badges")
    public ApiResponse<Void> selectMyPageBadges(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody UserReqDTO.BadgeSelectReqDTO request
            ){
        Long userId = user.getId();
        UserSuccessCode code = UserSuccessCode.MYPAGE_BADGES_OK;
        userService.selectMypageBadge(userId, request.badgeIds());
        return ApiResponse.onSuccess(code, null);
    }
}

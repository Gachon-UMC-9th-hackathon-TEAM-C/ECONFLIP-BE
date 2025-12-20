package com.example.econflip.domain.user.controller.UserCardController;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.exception.code.UserSuccessCode;
import com.example.econflip.domain.user.service.UserCardService;
import com.example.econflip.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LibraryPageController implements LibraryPageControllerDocs{
    private final UserCardService userCardService;

    @GetMapping("/api/library")
    public ApiResponse<UserCardResDTO.libraryPage> libraryPage(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestParam(required = false) CategoryType category
    ){
        if (category == null) {
            UserCardResDTO.libraryPage result = userCardService.getEntireLibraryPage(user.getId());

            UserSuccessCode code = UserSuccessCode.ENTIRE_LIBRARY_OK;
            return ApiResponse.onSuccess(code, result);
        } else {
            UserCardResDTO.libraryPage result = userCardService.getCategoryLibraryPage(user.getId(), category);

            UserSuccessCode code = UserSuccessCode.CATEGORY_LIBRARY_OK;
            return ApiResponse.onSuccess(code, result);
        }

    }
}

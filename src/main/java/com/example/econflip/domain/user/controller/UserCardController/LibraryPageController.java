package com.example.econflip.domain.user.controller.UserCardController;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.exception.code.UserSuccessCode;
import com.example.econflip.domain.user.service.UserCardService;
import com.example.econflip.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LibraryPageController implements LibraryPageControllerDocs{
    private final UserCardService userCardService;

    @GetMapping("/library")
    public ApiResponse<UserCardResDTO.libraryPage> libraryPage(
            Long userId,
            @RequestParam(required = false) CategoryType category
    ){
        if (category == null) {
            UserCardResDTO.libraryPage result = userCardService.getEntireLibraryPage(userId);

            UserSuccessCode code = UserSuccessCode.OK;
            return ApiResponse.onSuccess(code, result);
        } else {
            UserCardResDTO.libraryPage result = userCardService.getCategoryLibraryPage(userId, category);

            UserSuccessCode code = UserSuccessCode.OK;
            return ApiResponse.onSuccess(code, result);
        }

    }
}

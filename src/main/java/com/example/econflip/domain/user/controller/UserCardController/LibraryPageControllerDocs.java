package com.example.econflip.domain.user.controller.UserCardController;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Library", description = "라이브러리 페이지 API")
public interface LibraryPageControllerDocs {

    @Operation(
            summary = "라이브러리 페이지 조회",
            description = "카테고리는 금리, 물가, 투자, 재정 중 하나 선택 (default : 전체조회)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/api/library")
    public ApiResponse<UserCardResDTO.libraryPage> libraryPage(
            User user,
            @RequestParam(required = false) CategoryType category
    );
}

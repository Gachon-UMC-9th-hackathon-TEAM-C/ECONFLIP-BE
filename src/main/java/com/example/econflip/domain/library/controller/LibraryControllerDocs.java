package com.example.econflip.domain.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface LibraryControllerDocs {
    @Operation(
            summary = "모든 용어 조회 API",
            description = "용어를 모두 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")

    })
    String searchAllWord();

    @Operation(
            summary = "검색 키워드에 따른 용어 조회 API",
            description = "검색한 키워드를 포함한 용어를 모두 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")

    })
    String searchWordByKeyword(@PathVariable String keyword);

    @Operation(
            summary = "주제별 용어 조회 API",
            description = "선택한 주제의 용어를 모두 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")

    })
    String searchWordByTag(@PathVariable String tag);

    @Operation(
            summary = "용어 북마크 등록 API",
            description = "선택한 용어를 북마크로 등록합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")

    })
    String InsertBookmarkWord(@RequestBody Long cardId);

    @Operation(
            summary = "용어 북마크 삭제 API",
            description = "선택한 용어를 북마크에서 삭제합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")

    })
    String DeleteBookmarkWord(@RequestBody Long cardId);
}

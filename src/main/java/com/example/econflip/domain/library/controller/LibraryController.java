package com.example.econflip.domain.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class LibraryController implements LibraryControllerDocs{

    @GetMapping("/library")
    public String searchAllWord(){return "ok";}

    @GetMapping("/library/keywords/{keyword}")
    public String searchWordByKeyword(@PathVariable String keyword){
        return "search keyword = " + keyword;
    }

    @GetMapping("/library/tags/{tag}")
    public String searchWordByTag(@PathVariable String tag){
        return "search topic = " + tag;
    }

    @PostMapping("/bookmark")
    public String InsertBookmarkWord(@RequestBody Long cardId){
        return "Insert bookmark cardId = " + cardId;
    }

    @DeleteMapping("/bookmark")
    public String DeleteBookmarkWord(@RequestBody Long cardId){
        return "Delete bookmark cardId = " + cardId;
    }
}

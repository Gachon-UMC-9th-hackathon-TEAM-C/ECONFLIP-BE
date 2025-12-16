package com.example.econflip.domain.review.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class ReviewController implements ReviewControllerDocs{
    @GetMapping("/reviews")
    public String searchReviews(){
        return "ok";
    }

    @PostMapping("/reviews")
    public String insertReview(@RequestBody Long cardId){
        return "Insert review = " + cardId;
    }

    @DeleteMapping("/reviews")
    public String deleteReview(@RequestBody Long cardId){
        return "Delete review = " + cardId;
    }
}

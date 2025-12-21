package com.example.econflip.domain.card.dto;

public class CardReqDTO {
    public record QuizAnswer(
            String selectedTerm
    ){}
}

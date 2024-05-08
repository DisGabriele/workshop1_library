package it.paa.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class ReviewDTO {

    @NotBlank(message = "text must not be empty")
    private String text;

    @Min(value = 1,message = "score must be higher than 1")
    @Max(value = 5,message = "score must be lower than 5")
    @Column(name = "score")
    private Integer score;

    @Column(name = "date")
    @PastOrPresent(message = "date cannot be in the future")
    private LocalDate date;

    public ReviewDTO() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

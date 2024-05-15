package it.paa.model.dto;

import it.paa.model.entity.validation.book.IsADate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ReviewDTO {

    @NotBlank(message = "text must not be empty")
    private String text;

    @Min(value = 1,message = "score must be higher than 1")
    @Max(value = 5,message = "score must be lower than 5")
    private Integer score;

    @IsADate
    private String date;

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
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }
}

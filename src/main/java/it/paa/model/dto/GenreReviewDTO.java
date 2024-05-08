package it.paa.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.paa.model.entity.Genre;

import java.text.DecimalFormat;

public class GenreReviewDTO {
    private Genre genre;
    private Double averageReview;

    public GenreReviewDTO() {}

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @JsonIgnore
    public Double getAverageReviewDouble() {
        return averageReview;
    }

    public void setAverageReview(Double averageReview) {
        this.averageReview = averageReview;
    }

    public String getAverageReview() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(averageReview);
    }
}

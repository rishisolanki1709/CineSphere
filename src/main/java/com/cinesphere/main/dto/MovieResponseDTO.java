package com.cinesphere.main.dto;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponseDTO {

    private Long id;

    private String title;

    private String description;

    private String language;

    private Integer durationMinutes;

    private String genre;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    private Boolean active;

    private String posterUrl;
}
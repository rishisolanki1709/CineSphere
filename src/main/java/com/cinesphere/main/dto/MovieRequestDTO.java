package com.cinesphere.main.dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class MovieRequestDTO {
	@NotBlank(message = "Movie title is required")
	@Size(max = 100)
	private String title;

	@NotBlank(message = "Description is required")
	@Size(min = 10, max = 1000)
	private String description;

	@NotBlank(message = "Language is required")
	private String language;

	@NotNull(message = "Duration is required")
	@Positive(message = "Duration must be positive")
	private Integer durationMinutes;

	@NotBlank(message = "Genre is required")
	private String genre;

	@NotNull(message = "Release date is required")
	private LocalDate releaseDate;

	@NotNull(message = "Movie poster is required")
	private MultipartFile image;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(Integer durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}
}

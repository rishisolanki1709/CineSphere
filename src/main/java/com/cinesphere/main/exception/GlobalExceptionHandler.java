package com.cinesphere.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cinesphere.main.dto.ApiResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleEmailExists(EmailAlreadyExistsException ex) {

		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseDTO<>(false, ex.getMessage(), null));

	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleBadCredentials(BadCredentialsException ex) {

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ApiResponseDTO<>(false, "Invalid email or password", null));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleGeneric(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponseDTO<>(false, "Something went wrong : " + ex.getMessage(), null));
	}
}
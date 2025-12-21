package com.medilabo.patient.execption;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("timestamp", Instant.now().toString(), "status",
				404, "error", "Not Found", "message", ex.getMessage()));
	}
}
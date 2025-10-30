package com.medilabo.notes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateNoteRequest(
	    @NotNull @Min(1) Long patId,
	    @NotBlank String patient,
	    @NotBlank String note
	) {}
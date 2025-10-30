package com.medilabo.notes.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateNoteRequest(
    @NotBlank String note
) {}

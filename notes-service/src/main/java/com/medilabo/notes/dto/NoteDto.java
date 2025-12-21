package com.medilabo.notes.dto;

import java.time.Instant;

import com.medilabo.notes.model.Note;

public record NoteDto(String id, Long patId, String note, String createdAt) {
	public static NoteDto from(Note n) {
		return new NoteDto(n.getId(), n.getPatId(), n.getNote(),
				n.getCreatedAt() == null ? null : n.getCreatedAt().toString());
	}
}

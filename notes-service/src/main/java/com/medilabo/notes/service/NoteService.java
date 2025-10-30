package com.medilabo.notes.service;

import com.medilabo.notes.dto.CreateNoteRequest;
import com.medilabo.notes.dto.NoteDto;
import com.medilabo.notes.dto.UpdateNoteRequest;
import com.medilabo.notes.model.Note;

import java.util.List;
import java.util.Optional;

public interface NoteService {
	List<Note> findAll();

	List<Note> findByPatId(Long patId);

	Optional<Note> findById(String id);

	Note create(CreateNoteRequest req);

	Optional<Note> update(String id, UpdateNoteRequest req);

	boolean delete(String id);
}

package com.medilabo.notes.controller;

import com.medilabo.notes.dto.CreateNoteRequest;
import com.medilabo.notes.dto.NoteDto;
import com.medilabo.notes.dto.UpdateNoteRequest;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.service.NoteService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

/**
 * REST controller responsible for managing medical notes.
 *
 * This controller exposes CRUD endpoints to create, retrieve, update, and
 * delete notes associated with patients.
 */
@RestController
@RequestMapping(value = "/notes", produces = "application/json")
public class NoteController {

	private final NoteService service;

	/**
	 * Constructor injecting the note service.
	 *
	 * @param service service handling note business logic
	 */
	public NoteController(NoteService service) {
		this.service = service;
	}

	/**
	 * Retrieves all notes or notes filtered by patient ID.
	 *
	 * @param patientId optional patient identifier
	 * @return list of notes as DTOs
	 */
	@GetMapping
	public List<NoteDto> find(@RequestParam(required = false) Long patientId) {
		List<Note> notes = (patientId == null) ? service.findAll() : service.findByPatId(patientId);
		return notes.stream().map(NoteDto::from).toList();
	}

	/**
	 * Retrieves all notes for a specific patient.
	 *
	 * @param patientId patient identifier
	 * @return list of notes associated with the patient
	 */
	@GetMapping("/patient/{patientId}")
	public List<NoteDto> byPatient(@PathVariable Long patientId) {
		return service.findByPatId(patientId).stream().map(NoteDto::from).toList();
	}

	/**
	 * Retrieves a single note by its identifier.
	 *
	 * @param id note identifier
	 * @return the requested note
	 * @throws ResponseStatusException if the note does not exist
	 */
	@GetMapping("/{id}")
	public NoteDto getOne(@PathVariable String id) {
		return service.findById(id).map(NoteDto::from)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
	}

	/**
	 * Creates a new medical note.
	 *
	 * @param req request payload containing note data
	 * @return created note
	 */
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public NoteDto create(@Valid @RequestBody CreateNoteRequest req) {
		Note saved = service.create(req);
		return NoteDto.from(saved);
	}

	/**
	 * Updates an existing medical note.
	 *
	 * @param id  note identifier
	 * @param req request payload containing updated note content
	 * @return updated note
	 * @throws ResponseStatusException if the note does not exist
	 */
	@PutMapping(value = "/{id}", consumes = "application/json")
	public NoteDto update(@PathVariable String id, @Valid @RequestBody UpdateNoteRequest req) {
		Note updated = service.update(id, req)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
		return NoteDto.from(updated);
	}

	/**
	 * Deletes a medical note by its identifier.
	 *
	 * @param id note identifier
	 * @throws ResponseStatusException if the note does not exist
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String id) {
		if (!service.delete(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found");
		}
	}
}

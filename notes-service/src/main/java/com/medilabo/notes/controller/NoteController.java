package com.medilabo.notes.controller;

import com.medilabo.notes.dto.CreateNoteRequest;
import com.medilabo.notes.dto.NoteDto;
import com.medilabo.notes.dto.UpdateNoteRequest;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping(value = "/notes", produces = "application/json")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService service;
    
    public NoteController(NoteService service) {
    	this.service =service;
    }

    @GetMapping
    public List<NoteDto> find(@RequestParam(required = false) Long patientId) {
        List<Note> notes = (patientId == null)
                ? service.findAll()
                : service.findByPatId(patientId);
        return notes.stream().map(NoteDto::from).toList();
    }

    @GetMapping("/patient/{patientId}")
    public List<NoteDto> byPatient(@PathVariable Long patientId) {
        return service.findByPatId(patientId).stream().map(NoteDto::from).toList();
    }

    @GetMapping("/{id}")
    public NoteDto getOne(@PathVariable String id) {
        return service.findById(id)
                .map(NoteDto::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public NoteDto create(@Valid @RequestBody CreateNoteRequest req) {
        Note saved = service.create(req);
        return NoteDto.from(saved);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public NoteDto update(@PathVariable String id, @Valid @RequestBody UpdateNoteRequest req) {
        Note updated = service.update(id, req)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
        return NoteDto.from(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        if (!service.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found");
        }
    }
}
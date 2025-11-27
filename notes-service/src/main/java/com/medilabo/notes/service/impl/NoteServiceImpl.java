package com.medilabo.notes.service.impl;

import com.medilabo.notes.dto.CreateNoteRequest;
import com.medilabo.notes.dto.NoteDto;
import com.medilabo.notes.dto.UpdateNoteRequest;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;
import com.medilabo.notes.service.NoteService;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import java.time.Instant;


@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository repo;

    public NoteServiceImpl(NoteRepository repo) {
        this.repo = repo;
      }
    
    @Override
    public List<Note> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Note> findByPatId(Long patId) {
        return repo.findByPatId(patId);
    }

    @Override
    public Optional<Note> findById(String id) {
        return repo.findById(id);
    }

    @Override
    public Note create(CreateNoteRequest req) {
        Note n = new Note();
        n.setPatId(req.patId());
        n.setPatient(req.patient());
        n.setNote(req.note());
        n.setCreatedAt(Instant.now());
        return repo.save(n);
    }

    @Override
    public Optional<Note> update(String id, UpdateNoteRequest req) {
        return repo.findById(id).map(existing -> {
            existing.setNote(req.note());
            return repo.save(existing);
        });
    }

    @Override
    public boolean delete(String id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}

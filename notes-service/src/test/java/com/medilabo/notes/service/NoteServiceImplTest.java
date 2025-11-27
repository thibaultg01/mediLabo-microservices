package com.medilabo.notes.service;

import com.medilabo.notes.dto.CreateNoteRequest;
import com.medilabo.notes.dto.UpdateNoteRequest;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;
import com.medilabo.notes.service.impl.NoteServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository repo;

    @InjectMocks
    private NoteServiceImpl service;

    @Test
    void findAll_shouldReturnAllNotesFromRepository() {
        // GIVEN
        Note n1 = new Note();
        n1.setId("1");
        Note n2 = new Note();
        n2.setId("2");
        when(repo.findAll()).thenReturn(List.of(n1, n2));

        // WHEN
        List<Note> result = service.findAll();

        // THEN
        assertEquals(2, result.size());
        assertSame(n1, result.get(0));
        assertSame(n2, result.get(1));
        verify(repo, times(1)).findAll();
        verifyNoMoreInteractions(repo);
    }

    @Test
    void findByPatId_shouldDelegateToRepository() {
        // GIVEN
        Long patId = 42L;
        Note note = new Note();
        note.setPatId(patId);
        when(repo.findByPatId(patId)).thenReturn(List.of(note));

        // WHEN
        List<Note> result = service.findByPatId(patId);

        // THEN
        assertEquals(1, result.size());
        assertEquals(patId, result.get(0).getPatId());
        verify(repo, times(1)).findByPatId(patId);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void findById_shouldReturnNoteWhenPresent() {
        // GIVEN
        String id = "note-id";
        Note note = new Note();
        note.setId(id);
        when(repo.findById(id)).thenReturn(Optional.of(note));

        // WHEN
        Optional<Note> result = service.findById(id);

        // THEN
        assertTrue(result.isPresent());
        assertSame(note, result.get());
        verify(repo, times(1)).findById(id);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        // GIVEN
        String id = "missing-id";
        when(repo.findById(id)).thenReturn(Optional.empty());

        // WHEN
        Optional<Note> result = service.findById(id);

        // THEN
        assertTrue(result.isEmpty());
        verify(repo, times(1)).findById(id);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void create_shouldBuildNoteAndSaveIt() {
        // GIVEN
        CreateNoteRequest req = new CreateNoteRequest(1L, "Test Patient", "Some note");
        // on renvoie simplement l’argument passé à save
        when(repo.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        Note saved = service.create(req);

        // THEN
        assertNotNull(saved);
        assertEquals(1L, saved.getPatId());
        assertEquals("Test Patient", saved.getPatient());
        assertEquals("Some note", saved.getNote());
        assertNotNull(saved.getCreatedAt());

        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(repo, times(1)).save(captor.capture());
        Note toSave = captor.getValue();
        assertEquals(1L, toSave.getPatId());
        assertEquals("Test Patient", toSave.getPatient());
        assertEquals("Some note", toSave.getNote());
        assertNotNull(toSave.getCreatedAt());

        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_shouldUpdateExistingNoteAndSave() {
        // GIVEN
        String id = "note-id";
        Note existing = new Note();
        existing.setId(id);
        existing.setPatId(1L);
        existing.setPatient("Test");
        existing.setNote("Old note");
        existing.setCreatedAt(Instant.now().minusSeconds(3600));

        UpdateNoteRequest req = new UpdateNoteRequest("New note");

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        Optional<Note> result = service.update(id, req);

        // THEN
        assertTrue(result.isPresent());
        Note updated = result.get();
        assertEquals("New note", updated.getNote());
        // patId, patient et createdAt ne doivent pas être modifiés
        assertEquals(1L, updated.getPatId());
        assertEquals("Test", updated.getPatient());
        assertNotNull(updated.getCreatedAt());

        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(existing);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_shouldReturnEmptyWhenNoteDoesNotExist() {
        // GIVEN
        String id = "missing-id";
        UpdateNoteRequest req = new UpdateNoteRequest("New note");
        when(repo.findById(id)).thenReturn(Optional.empty());

        // WHEN
        Optional<Note> result = service.update(id, req);

        // THEN
        assertTrue(result.isEmpty());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
        verifyNoMoreInteractions(repo);
    }

    @Test
    void delete_shouldDeleteAndReturnTrueWhenExists() {
        // GIVEN
        String id = "note-id";
        when(repo.existsById(id)).thenReturn(true);

        // WHEN
        boolean deleted = service.delete(id);

        // THEN
        assertTrue(deleted);
        verify(repo, times(1)).existsById(id);
        verify(repo, times(1)).deleteById(id);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void delete_shouldReturnFalseWhenNotExists() {
        // GIVEN
        String id = "missing-id";
        when(repo.existsById(id)).thenReturn(false);

        // WHEN
        boolean deleted = service.delete(id);

        // THEN
        assertFalse(deleted);
        verify(repo, times(1)).existsById(id);
        verify(repo, never()).deleteById(anyString());
        verifyNoMoreInteractions(repo);
    }
}

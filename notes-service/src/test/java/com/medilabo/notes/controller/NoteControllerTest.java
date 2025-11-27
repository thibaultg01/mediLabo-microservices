package com.medilabo.notes.controller;

import com.medilabo.notes.dto.CreateNoteRequest;
import com.medilabo.notes.dto.UpdateNoteRequest;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.service.NoteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService service;

    private Note buildNote(String id, Long patId, String noteText, Instant createdAt) {
        Note n = new Note();
        n.setId(id);
        n.setPatId(patId);
        n.setPatient("John Doe");
        n.setNote(noteText);
        n.setCreatedAt(createdAt);
        return n;
    }

    @Test
    void findAll_shouldReturnListOfNotes() throws Exception {
        // GIVEN
        Note n1 = buildNote("1", 1L, "Note 1", Instant.parse("2024-01-01T10:00:00Z"));
        Note n2 = buildNote("2", 2L, "Note 2", Instant.parse("2024-01-02T11:00:00Z"));
        given(service.findAll()).willReturn(List.of(n1, n2));

        // WHEN / THEN
        mockMvc.perform(get("/notes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].patId", is(1)))
                .andExpect(jsonPath("$[0].note", is("Note 1")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].patId", is(2)))
                .andExpect(jsonPath("$[1].note", is("Note 2")));

        verify(service).findAll();
    }

    @Test
    void findByPatId_shouldReturnNotesForPatient() throws Exception {
        // GIVEN
        Long patId = 3L;
        Note n1 = buildNote("1", patId, "Note patient 3", Instant.parse("2024-01-01T10:00:00Z"));
        given(service.findByPatId(patId)).willReturn(List.of(n1));

        // WHEN / THEN
        mockMvc.perform(get("/notes/patient/{patId}", patId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patId", is(3)))
                .andExpect(jsonPath("$[0].note", is("Note patient 3")));

        verify(service).findByPatId(patId);
    }

    @Test
    void findById_shouldReturnNote_whenExists() throws Exception {
        // GIVEN
        String id = "note-id";
        Note n = buildNote(id, 5L, "Some note", Instant.parse("2024-01-03T12:00:00Z"));
        given(service.findById(id)).willReturn(Optional.of(n));

        // WHEN / THEN
        mockMvc.perform(get("/notes/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.patId", is(5)))
                .andExpect(jsonPath("$.note", is("Some note")));

        verify(service).findById(id);
    }

    @Test
    void findById_shouldReturn404_whenNotFound() throws Exception {
        // GIVEN
        String id = "missing-id";
        given(service.findById(id)).willReturn(Optional.empty());

        // WHEN / THEN
        mockMvc.perform(get("/notes/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).findById(id);
    }

    @Test
    void create_shouldReturnCreatedNote_with201() throws Exception {
        // GIVEN
        String body = """
            {
              "patId": 10,
              "patient": "John Doe",
              "note": "New note"
            }
            """;

        Note created = buildNote("new-id", 10L, "New note", Instant.parse("2024-01-04T09:00:00Z"));
        given(service.create(any(CreateNoteRequest.class))).willReturn(created);

        // WHEN / THEN
        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("new-id")))
                .andExpect(jsonPath("$.patId", is(10)))
                .andExpect(jsonPath("$.note", is("New note")));

        Mockito.verify(service).create(any(CreateNoteRequest.class));
    }

    @Test
    void create_shouldReturn400_whenValidationFails() throws Exception {
        // note vide + patient vide -> viole @NotBlank
        String invalidBody = """
            {
              "patId": 10,
              "patient": "",
              "note": ""
            }
            """;

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturnUpdatedNote_whenExists() throws Exception {
        // GIVEN
        String id = "note-id";
        String body = """
            {
              "note": "Updated note"
            }
            """;

        Note updated = buildNote(id, 7L, "Updated note", Instant.parse("2024-01-05T10:00:00Z"));
        given(service.update(eq(id), any(UpdateNoteRequest.class))).willReturn(Optional.of(updated));

        // WHEN / THEN
        mockMvc.perform(put("/notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.patId", is(7)))
                .andExpect(jsonPath("$.note", is("Updated note")));

        verify(service).update(eq(id), any(UpdateNoteRequest.class));
    }

    @Test
    void update_shouldReturn404_whenNoteNotFound() throws Exception {
        // GIVEN
        String id = "missing-id";
        String body = """
            {
              "note": "Updated note"
            }
            """;

        given(service.update(eq(id), any(UpdateNoteRequest.class))).willReturn(Optional.empty());

        // WHEN / THEN
        mockMvc.perform(put("/notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());

        verify(service).update(eq(id), any(UpdateNoteRequest.class));
    }

    @Test
    void delete_shouldReturn204_whenDeleted() throws Exception {
        // GIVEN
        String id = "note-id";
        given(service.delete(id)).willReturn(true);

        // WHEN / THEN
        mockMvc.perform(delete("/notes/{id}", id))
                .andExpect(status().isNoContent());

        verify(service).delete(id);
    }

    @Test
    void delete_shouldReturn404_whenNoteNotFound() throws Exception {
        // GIVEN
        String id = "missing-id";
        given(service.delete(id)).willReturn(false);

        // WHEN / THEN
        mockMvc.perform(delete("/notes/{id}", id))
                .andExpect(status().isNotFound());

        verify(service).delete(id);
    }
}

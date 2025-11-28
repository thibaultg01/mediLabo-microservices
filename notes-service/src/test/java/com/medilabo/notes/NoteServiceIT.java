package com.medilabo.notes;

import com.medilabo.notes.dto.CreateNoteRequest;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;
import com.medilabo.notes.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class NoteServiceIT {

	@Container
	static MongoDBContainer mongo = new MongoDBContainer("mongo:7");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
	}

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private NoteService noteService;

	@Test
	void createNoteAndFindByPatId() {
		Long patId = 1L;
		CreateNoteRequest req = new CreateNoteRequest(patId, "Test Patient", "First test note from Testcontainers");

		Note saved = noteService.create(req);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getPatId()).isEqualTo(patId);

		List<Note> notes = noteRepository.findByPatIdOrderByCreatedAtDesc(patId);
		assertThat(notes).hasSize(1);
		assertThat(notes.get(0).getNote()).isEqualTo("First test note from Testcontainers");
	}
}

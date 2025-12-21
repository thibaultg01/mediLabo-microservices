package com.medilabo.notes.repository;

import com.medilabo.notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * MongoDB repository for {@link Note} entities.
 *
 * Provides methods to retrieve notes associated with a specific patient.
 */
public interface NoteRepository extends MongoRepository<Note, String> {
	/**
	 * Retrieves all notes for a patient ordered by creation date descending.
	 *
	 * @param patId patient identifier
	 * @return list of notes ordered from most recent to oldest
	 */
	List<Note> findByPatIdOrderByCreatedAtDesc(Long patId);

	/**
	 * Retrieves all notes for a given patient.
	 *
	 * @param patId patient identifier
	 * @return list of notes
	 */
	List<Note> findByPatId(Long patId);
}
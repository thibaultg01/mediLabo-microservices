package com.medilabo.patient.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import com.medilabo.patient.service.PatientService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PatientServiceImpl implements PatientService {

	private final PatientRepository repo;

	/**
	 * Constructor injecting the patient repository.
	 *
	 * @param repo repository used for patient persistence
	 */
	public PatientServiceImpl(PatientRepository repo) {
		this.repo = repo;
	}

	/**
	 * Retrieves all patients.
	 *
	 * @return list of all patients
	 */
	@Override
	public List<Patient> findAll() {
		return repo.findAll();
	}

	/**
	 * Retrieves a patient by its identifier.
	 *
	 * @param id patient identifier
	 * @return the requested patient
	 * @throws EntityNotFoundException if the patient does not exist
	 */
	@Override
	public Patient findById(Long id) {
		return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient " + id + " not found"));
	}

	/**
	 * Creates a new patient.
	 *
	 * The patient identifier is reset to ensure a new entity is persisted.
	 *
	 * @param p patient to create
	 * @return persisted patient
	 */
	@Override
	public Patient create(Patient p) {
		p.setId(null);
		return repo.save(p);
	}

	/**
	 * Updates an existing patient.
	 *
	 * Only mutable fields are updated; the identifier remains unchanged.
	 *
	 * @param id patient identifier
	 * @param p  updated patient data
	 * @return updated patient
	 */
	@Override
	public Patient update(Long id, Patient p) {
		Patient existing = findById(id);
		existing.setFirstName(p.getFirstName());
		existing.setLastName(p.getLastName());
		existing.setBirthDate(p.getBirthDate());
		existing.setGender(p.getGender());
		existing.setAddress(p.getAddress());
		existing.setPhone(p.getPhone());
		return repo.save(existing);
	}
}
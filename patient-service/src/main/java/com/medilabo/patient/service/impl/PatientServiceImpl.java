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

  public PatientServiceImpl(PatientRepository repo) {
    this.repo = repo;
  }

  @Override public List<Patient> findAll() { return repo.findAll(); }

  @Override public Patient findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient " + id + " not found"));
  }

  @Override public Patient create(Patient p) { p.setId(null); return repo.save(p); }

  @Override public void update(Long id, Patient p) {
    Patient existing = findById(id);
    existing.setFirstName(p.getFirstName());
    existing.setLastName(p.getLastName());
    existing.setBirthDate(p.getBirthDate());
    existing.setGender(p.getGender());
    existing.setAddress(p.getAddress());
    existing.setPhone(p.getPhone());
    repo.save(existing);
  }
}
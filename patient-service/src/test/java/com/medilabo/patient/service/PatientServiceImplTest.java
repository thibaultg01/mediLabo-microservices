package com.medilabo.patient.service;

import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import com.medilabo.patient.service.impl.PatientServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

  @Mock PatientRepository repo;
  @InjectMocks PatientServiceImpl service;

  private Patient sample(Long id) {
    Patient p = new Patient();
    p.setId(id);
    p.setFirstName("John");
    p.setLastName("Smith");
    p.setBirthDate(LocalDate.of(1990,1,1));
    p.setGender("M");
    p.setAddress("1 rue de Paris");
    p.setPhone("0102030405");
    return p;
  }

  @Test
  void findAll_ok() {
    when(repo.findAll()).thenReturn(List.of(sample(1L), sample(2L)));
    List<Patient> result = service.findAll();
    assertThat(result).hasSize(2);
    verify(repo).findAll();
  }

  @Test
  void findById_found() {
    when(repo.findById(1L)).thenReturn(Optional.of(sample(1L)));
    Patient result = service.findById(1L);
    assertThat(result.getId()).isEqualTo(1L);
    verify(repo).findById(1L);
  }

  @Test
  void findById_notFound() {
    when(repo.findById(99L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.findById(99L))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void create_ok() {
    Patient toCreate = sample(null);
    Patient saved = sample(10L);
    when(repo.save(any(Patient.class))).thenReturn(saved);

    Patient result = service.create(toCreate);

    assertThat(result.getId()).isEqualTo(10L);
    ArgumentCaptor<Patient> captor = ArgumentCaptor.forClass(Patient.class);
    verify(repo).save(captor.capture());
    Patient passed = captor.getValue();
    assertThat(passed.getId()).isNull();
    assertThat(passed.getFirstName()).isEqualTo("John");
  }

  @Test
  void update_ok() {
    Patient existing = sample(5L);
    when(repo.findById(5L)).thenReturn(Optional.of(existing));

    Patient incoming = new Patient();
    incoming.setFirstName("Jane");
    incoming.setLastName("Smith");
    incoming.setBirthDate(LocalDate.of(1988, 5, 20));
    incoming.setGender("F");
    incoming.setAddress("2 avenue de Lyon");
    incoming.setPhone("0606060606");

    Patient saved = sample(5L);
    saved.setFirstName("Jane");
    saved.setLastName("Smith");
    saved.setBirthDate(LocalDate.of(1988, 5, 20));
    saved.setGender("F");
    saved.setAddress("2 avenue de Lyon");
    saved.setPhone("0606060606");
    when(repo.save(any(Patient.class))).thenReturn(saved);

    Patient result = service.update(5L, incoming);

    assertThat(result.getId()).isEqualTo(5L);
    assertThat(result.getFirstName()).isEqualTo("Jane");
    assertThat(result.getLastName()).isEqualTo("Smith");
    assertThat(result.getGender()).isEqualTo("F");
    assertThat(result.getAddress()).isEqualTo("2 avenue de Lyon");
    assertThat(result.getPhone()).isEqualTo("0606060606");

    ArgumentCaptor<Patient> captor = ArgumentCaptor.forClass(Patient.class);
    verify(repo).save(captor.capture());
    Patient passed = captor.getValue();
    assertThat(passed.getId()).isEqualTo(5L);
    assertThat(passed.getFirstName()).isEqualTo("Jane");
  }
}

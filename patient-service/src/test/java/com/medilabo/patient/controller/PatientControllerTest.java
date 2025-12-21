package com.medilabo.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.patient.model.Patient;
import com.medilabo.patient.service.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

	@Autowired
	MockMvc mvc;
	@Autowired
	ObjectMapper om;

	@MockBean
	PatientService service;

	private Patient sample(Long id) {
		Patient p = new Patient();
		p.setId(id);
		p.setFirstName("John");
		p.setLastName("Smith");
		p.setBirthDate(LocalDate.of(1990, 1, 1));
		p.setGender("M");
		p.setAddress("1 rue de Paris");
		p.setPhone("0102030405");
		return p;
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	void getAll_returns200() throws Exception {
		Mockito.when(service.findAll()).thenReturn(List.of(sample(1L), sample(2L)));

		mvc.perform(get("/patients")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[1].id", is(2)));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	void getById_returns200() throws Exception {
		Mockito.when(service.findById(1L)).thenReturn(sample(1L));

		mvc.perform(get("/patients/{id}", 1)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.firstName", is("John")));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	void create_returns201_andLocation() throws Exception {
		Patient payload = sample(null);
		Patient created = sample(10L);
		Mockito.when(service.create(any(Patient.class))).thenReturn(created);

		mvc.perform(post("/patients").with(httpBasic("user", "password")).with(csrf())
				.contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(payload)))
				.andExpect(status().isCreated()).andExpect(header().string("Location", "/patients/10"))
				.andExpect(jsonPath("$.id", is(10)));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	void update_returns200() throws Exception {
		Patient payload = sample(null);
		Patient updated = sample(5L);
		updated.setFirstName("Jane");
		Mockito.when(service.update(eq(5L), any(Patient.class))).thenReturn(updated);

		mvc.perform(put("/patients/{id}", 5).with(httpBasic("user", "password")).with(csrf())
				.contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(payload)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(5)))
				.andExpect(jsonPath("$.firstName", is("Jane")));
	}
}

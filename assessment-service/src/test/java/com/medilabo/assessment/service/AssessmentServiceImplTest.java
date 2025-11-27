package com.medilabo.assessment.service;

import com.medilabo.assessment.dto.AssessmentDto;
import com.medilabo.assessment.model.RiskLevel;
import com.medilabo.assessment.service.impl.AssessmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AssessmentServiceImplTest {

	private RestClient restClient;
	private MockRestServiceServer server;
	private AssessmentServiceImpl service;

	private static final String PATIENTS_BASE = "http://mock/patients";
	private static final String NOTES_BASE = "http://mock/notes";

	@BeforeEach
	void setUp() {
		RestClient.Builder builder = RestClient.builder();

		server = MockRestServiceServer.bindTo(builder).build();

		restClient = builder.build();

		service = new AssessmentServiceImpl(restClient, PATIENTS_BASE, NOTES_BASE);
	}

	@Test
	void assess_patient1_None() {
		server.expect(once(), requestTo(PATIENTS_BASE + "/1")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("""
						{"id":1,"firstName":"Test","lastName":"TestNone","birthDate":"1966-12-31","sex":"F"}
						""", MediaType.APPLICATION_JSON));

		server.expect(once(), requestTo(NOTES_BASE + "/patient/1")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(
						"""
								[
								  {"id":"n1","patId":1,"patient":"TestNone","note":"Poids égal ou inférieur au poids recommandé","createdAt":"2023-01-05T09:00:00Z"}
								]
								""",
						MediaType.APPLICATION_JSON));

		AssessmentDto dto = service.assessByPatientId(1L);
		server.verify();

		assertNotNull(dto);
		assertEquals("TestNone", dto.getPatient());
		assertTrue(dto.getAge() > 30);
		assertEquals(1, dto.getTriggerCount());
		assertEquals(RiskLevel.NONE, dto.getRiskLevel());
	}

	@Test
	void assess_patient2_Borderline() {
		// Patient 2 : >30 ans ; 2 triggers (anormale → "anormal", reaction)
		// BORDERLINE
		server.expect(once(), requestTo(PATIENTS_BASE + "/2")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("""
						{"id":2,"firstName":"Test","lastName":"TestBorderline","birthDate":"1945-06-24","sex":"M"}
						""", MediaType.APPLICATION_JSON));

		server.expect(once(), requestTo(NOTES_BASE + "/patient/2")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(
						"""
								[
								  {"id":"n2a","patId":2,"patient":"TestBorderline","note":"Audition anormale récemment","createdAt":"2023-02-10T10:00:00Z"},
								  {"id":"n2b","patId":2,"patient":"TestBorderline","note":"Réaction aux médicaments récente","createdAt":"2023-04-10T10:00:00Z"}
								]
								""",
						MediaType.APPLICATION_JSON));

		AssessmentDto dto = service.assessByPatientId(2L);
		server.verify();

		assertNotNull(dto);
		assertEquals("TestBorderline", dto.getPatient());
		assertTrue(dto.getAge() > 30);
		assertEquals(2, dto.getTriggerCount());
		assertEquals(RiskLevel.BORDERLINE, dto.getRiskLevel());
	}

	@Test
	void assess_patient3_InDanger() {
		// Patient 3 : <30 ans, M ; 3 triggers (fumeur, cholesterol, reaction)
		// IN_DANGER
		server.expect(once(), requestTo(PATIENTS_BASE + "/3")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("""
						{"id":3,"firstName":"Test","lastName":"TestInDanger","birthDate":"2004-06-18","sex":"M"}
						""", MediaType.APPLICATION_JSON));

		server.expect(once(), requestTo(NOTES_BASE + "/patient/3")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(
						"""
								[
								  {"id":"n3a","patId":3,"patient":"TestInDanger","note":"Le patient est fumeur depuis peu","createdAt":"2023-03-10T10:00:00Z"},
								  {"id":"n3b","patId":3,"patient":"TestInDanger","note":"cholestérol LDL élevé","createdAt":"2023-05-10T10:00:00Z"},
								  {"id":"n3c","patId":3,"patient":"TestInDanger","note":"réaction légère","createdAt":"2023-06-10T10:00:00Z"}
								]
								""",
						MediaType.APPLICATION_JSON));

		AssessmentDto dto = service.assessByPatientId(3L);
		server.verify();

		assertNotNull(dto);
		assertEquals("TestInDanger", dto.getPatient());
		assertTrue(dto.getAge() < 30);
		assertEquals(3, dto.getTriggerCount());
		assertEquals(RiskLevel.IN_DANGER, dto.getRiskLevel());
	}

	@Test
	void assess_patient4_EarlyOnset() {
		// Patient 4 : <30 ans, F ; EarlyOnset
		// mot clés: hemoglobine a1c, taille, poids, fumeuse, cholesterol,
		server.expect(once(), requestTo(PATIENTS_BASE + "/4")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess("""
						{"id":4,"firstName":"Test","lastName":"TestEarlyOnset","birthDate":"2002-06-28","sex":"F"}
						""", MediaType.APPLICATION_JSON));

		server.expect(once(), requestTo(NOTES_BASE + "/patient/4")).andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(
						"""
								[
								  {"id":"n4a","patId":4,"patient":"TestEarlyOnset","note":"Tests labo: anticorps élevés. Réaction observée.","createdAt":"2023-01-10T10:00:00Z"},
								  {"id":"n4b","patId":4,"patient":"TestEarlyOnset","note":"La patiente est fumeuse. Hémoglobine A1C au-dessus du seuil.","createdAt":"2023-02-10T10:00:00Z"},
								  {"id":"n4c","patId":4,"patient":"TestEarlyOnset","note":"Taille et Poids relevés. Cholestérol normalisé." ,"createdAt":"2023-03-10T10:00:00Z"},
								  {"id":"n4d","patId":4,"patient":"TestEarlyOnset","note":"Vertiges persistants." ,"createdAt":"2023-04-10T10:00:00Z"}
								]
								""",
						MediaType.APPLICATION_JSON));

		AssessmentDto dto = service.assessByPatientId(4L);
		server.verify();

		assertNotNull(dto);
		assertEquals("TestEarlyOnset", dto.getPatient());
		assertTrue(dto.getAge() < 30);
		assertEquals(8, dto.getTriggerCount());
		assertEquals(RiskLevel.EARLY_ONSET, dto.getRiskLevel());
	}
}

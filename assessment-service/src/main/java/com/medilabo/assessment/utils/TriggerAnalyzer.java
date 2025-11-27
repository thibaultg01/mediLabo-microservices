package com.medilabo.assessment.utils;

import com.medilabo.assessment.controller.AssessmentController;
import com.medilabo.assessment.dto.NoteDto;

import java.text.Normalizer;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TriggerAnalyzer {

	 private static final Logger logger = LogManager.getLogger(TriggerAnalyzer.class);
	 
	private static final Set<String> TRIGGERS = Set.of("hemoglobine a1c", "microalbumine", "taille", "poids", "fumeur",
			"fumeuse", "anormal", "cholesterol", "vertige", "rechute", "reaction",
			"anticorps");

	private TriggerAnalyzer() {
	}

	public static int countTriggers(List<NoteDto> notes) {
		if (notes == null || notes.isEmpty())
			return 0;

		String all = notes.stream().map(NoteDto::getNote).filter(s -> s != null && !s.isBlank())
				.map(TriggerAnalyzer::normalize).reduce("", (a, b) -> a + " " + b);

		int count = 0;
		for (String t : TRIGGERS) {
			if (all.contains(t))
			{
				logger.info(t);
				count++;
			}
				
		}
		return count;
	}

	private static String normalize(String s) {
		String lower = s.toLowerCase();
		String norm = Normalizer.normalize(lower, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		return norm.replace("cholésterol", "cholesterol").replace("cholestérol", "cholesterol");
	}
}

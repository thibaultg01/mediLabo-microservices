package com.medilabo.assessment.utils;

import com.medilabo.assessment.dto.NoteDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TriggerAnalyzerTest {

  private static NoteDto note(String text) {
    NoteDto n = new NoteDto();
    n.setNote(text);
    return n;
  }


  @Test
  void returns_zero_when_notes_null_or_empty() {
    assertEquals(0, TriggerAnalyzer.countTriggers(null));
    assertEquals(0, TriggerAnalyzer.countTriggers(List.of()));
  }

  @Test
  void detects_basic_triggers_case_and_accents_ignored() {
    // Hémoglobine A1C, Microalbumine, Taille, Poids
    List<NoteDto> notes = List.of(
        note("Hémoglobine A1C au-dessus du seuil."),
        note("microalbumine présente."),
        note("TAILLE correcte."),
        note("Poids égal ou inférieur au recommandé.")
    );
    assertEquals(4, TriggerAnalyzer.countTriggers(notes));
  }

  @Test
  void anormal_matches_anormale_and_anormales_once() {
    List<NoteDto> notes = List.of(
        note("Audition anormale récemment."),
        note("Analyses anormales hier.")
    );
    assertEquals(1, TriggerAnalyzer.countTriggers(notes)); 
  }

  @Test
  void cholesterol_with_accents_is_detected() {
    List<NoteDto> notes = List.of(
        note("Cholestérol total élevé.")
    );
    assertEquals(1, TriggerAnalyzer.countTriggers(notes));
  }

  @Test
  void fumeur_and_fumeuse_are_counted_as_two_distinct_triggers_in_this_version() {
    List<NoteDto> notes = List.of(
        note("Le patient est fumeur."),
        note("La patiente précédente était fumeuse.")
    );
    assertEquals(2, TriggerAnalyzer.countTriggers(notes)); 
  }

  @Test
  void vertiges_plural_may_double_count_with_vertige_in_current_list() {
    List<NoteDto> notes = List.of(
        note("Vertiges fréquents au lever.")
    );
    assertEquals(2, TriggerAnalyzer.countTriggers(notes));
  }

  @Test
  void early_onset_sample_from_spec_expect_7_without_counting_fumer() {
    List<NoteDto> notes = List.of(
        note("Tests de laboratoire indiquant que les anticorps sont élevés. Réaction aux médicaments."),
        note("Le patient déclare avoir commencé à fumer depuis peu. Hémoglobine A1C supérieure au niveau recommandé."),
        note("Taille, Poids, Cholestérol, Vertige et Réaction.")
    );
    //  anticorps, reaction, hemoglobine a1c, taille, poids, cholesterol, vertige
    assertEquals(7, TriggerAnalyzer.countTriggers(notes));
  }
}

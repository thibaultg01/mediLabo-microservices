package com.medilabo.assessment.dto;

import java.util.List;

public class NlpExtractRequest {

    private String language = "fr";
    private List<NoteItem> notes;

    public NlpExtractRequest(List<NoteItem> notes) {
        this.notes = notes;
    }

    public String getLanguage() { return language; }
    public List<NoteItem> getNotes() { return notes; }

    public static class NoteItem {
        private String id;
        private String text;

        public NoteItem(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public String getId() { return id; }
        public String getText() { return text; }
    }
}

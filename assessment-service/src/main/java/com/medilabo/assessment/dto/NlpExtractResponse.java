package com.medilabo.assessment.dto;

import java.util.List;
import java.util.Map;

public class NlpExtractResponse {

    private List<MatchItem> matches;
    private Map<String, Integer> triggerCounts;
    private int totalTriggers;
    private Map<String, String> explanations;

    public List<MatchItem> getMatches() { return matches; }
    public Map<String, Integer> getTriggerCounts() { return triggerCounts; }
    public int getTotalTriggers() { return totalTriggers; }
    public Map<String, String> getExplanations() { return explanations; }

    public static class MatchItem {
        private String noteId;
        private List<String> triggers;

        public String getNoteId() { return noteId; }
        public List<String> getTriggers() { return triggers; }
    }
}

package com.bhadra.jobtracker.dto;

import lombok.Data;
import java.util.List;

@Data
public class AiAnalysisResponse {
    private int matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<String> suggestions;
    private String summary;
}
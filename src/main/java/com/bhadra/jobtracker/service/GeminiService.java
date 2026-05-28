package com.bhadra.jobtracker.service;

import com.bhadra.jobtracker.dto.AiAnalysisResponse;
import com.bhadra.jobtracker.dto.ChatMessage;
import com.bhadra.jobtracker.dto.ChatResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Transactional
@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private final ObjectMapper objectMapper;

    // Core method — calls Groq (Llama 3) API
    private String callGroq(String prompt) {
        WebClient client = WebClient.create();

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7,
                "max_tokens", 1024
        );

        String response = client.post()
                .uri(apiUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(response);
            return root
                    .path("choices").get(0)
                    .path("message")
                    .path("content")
                    .asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response: " + e.getMessage());
        }
    }

    // Feature 1 — JD Analyzer
    public AiAnalysisResponse analyzeJobDescription(String jobDescription, List<String> userSkills) {
        String prompt = """
                You are a career advisor AI. Analyze this job description against the candidate's skills.
                
                Job Description:
                %s
                
                Candidate Skills:
                %s
                
                Respond ONLY with a valid JSON object in exactly this format, no extra text, no markdown:
                {
                  "matchScore": <number 0-100>,
                  "matchedSkills": ["skill1", "skill2"],
                  "missingSkills": ["skill1", "skill2"],
                  "suggestions": ["suggestion1", "suggestion2", "suggestion3"],
                  "summary": "2 sentence summary of fit"
                }
                """.formatted(jobDescription, String.join(", ", userSkills));

        String rawResponse = callGroq(prompt);

        try {
            String cleaned = rawResponse
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();
            return objectMapper.readValue(cleaned, AiAnalysisResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI analysis: " + e.getMessage());
        }
    }

    // Feature 2 — Chat assistant
    public ChatResponse chat(String userMessage, List<ChatMessage> history) {
        StringBuilder conversation = new StringBuilder();
        conversation.append("""
                You are a helpful career assistant for software engineering job seekers in India.
                Help with interview preparation, resume advice, cold emails, and career guidance.
                Keep responses concise and practical.
                
                Conversation so far:
                """);

        int start = Math.max(0, history.size() - 5);
        for (int i = start; i < history.size(); i++) {
            ChatMessage msg = history.get(i);
            conversation.append(msg.getRole().toUpperCase())
                    .append(": ")
                    .append(msg.getContent())
                    .append("\n");
        }
        conversation.append("USER: ").append(userMessage);

        String reply = callGroq(conversation.toString());
        return new ChatResponse(reply);
    }

    // Feature 3 — Summarize JD
    public String summarizeJd(String jobDescription) {
        String prompt = """
                Summarize this job description in exactly 2 sentences.
                Focus on: role, key skills required, and company type.
                Job Description: %s
                """.formatted(jobDescription);

        return callGroq(prompt);
    }
}
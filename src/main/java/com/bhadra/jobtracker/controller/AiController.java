package com.bhadra.jobtracker.controller;

import com.bhadra.jobtracker.dto.*;
import com.bhadra.jobtracker.entity.User;
import com.bhadra.jobtracker.repository.UserRepository;
import com.bhadra.jobtracker.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final GeminiService geminiService;
    private final UserRepository userRepository;

    // Feature 1 — Analyze JD against user's saved skills
    @PostMapping("/analyze")
    public ResponseEntity<AiAnalysisResponse> analyzeJd(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> body) {

        String jobDescription = body.get("jobDescription");

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> skills = user.getSkills() != null && !user.getSkills().isEmpty()
                ? new ArrayList<>(user.getSkills())
                : List.of("Java", "Spring Boot", "React");

        return ResponseEntity.ok(
                geminiService.analyzeJobDescription(jobDescription, skills));
    }
    // Feature 2 — Chat assistant
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChatRequest request) {

        return ResponseEntity.ok(
                geminiService.chat(request.getMessage(), request.getHistory()));
    }

    // Feature 3 — Summarize a JD
    @PostMapping("/summarize")
    public ResponseEntity<String> summarize(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok(
                geminiService.summarizeJd(body.get("jobDescription")));
    }
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("AI controller is working");
    }
}
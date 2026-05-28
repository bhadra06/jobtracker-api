package com.bhadra.jobtracker.controller;

import com.bhadra.jobtracker.dto.*;
import com.bhadra.jobtracker.entity.ApplicationStatus;
import com.bhadra.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<JobApplicationResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody JobApplicationDto dto) {
        return ResponseEntity.ok(
                jobApplicationService.create(userDetails.getUsername(), dto));
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAll(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) ApplicationStatus status) {
        if (status != null) {
            return ResponseEntity.ok(
                    jobApplicationService.getByStatus(userDetails.getUsername(), status));
        }
        return ResponseEntity.ok(
                jobApplicationService.getAll(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                jobApplicationService.getById(userDetails.getUsername(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationDto dto) {
        return ResponseEntity.ok(
                jobApplicationService.update(userDetails.getUsername(), id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        jobApplicationService.delete(userDetails.getUsername(), id);
        return ResponseEntity.ok("Application deleted");
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStats(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                jobApplicationService.getStats(userDetails.getUsername()));
    }
}
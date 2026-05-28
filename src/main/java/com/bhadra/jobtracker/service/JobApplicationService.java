package com.bhadra.jobtracker.service;

import com.bhadra.jobtracker.dto.*;
import com.bhadra.jobtracker.entity.*;
import com.bhadra.jobtracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private JobApplicationResponse toResponse(JobApplication app) {
        JobApplicationResponse res = new JobApplicationResponse();
        res.setId(app.getId());
        res.setCompanyName(app.getCompanyName());
        res.setRoleName(app.getRoleName());
        res.setJobDescription(app.getJobDescription());
        res.setJobUrl(app.getJobUrl());
        res.setStatus(app.getStatus());
        res.setPriority(app.getPriority());
        res.setAppliedDate(app.getAppliedDate());
        res.setFollowUpDate(app.getFollowUpDate());
        res.setNotes(app.getNotes());
        res.setAiMatchScore(app.getAiMatchScore());
        res.setCreatedAt(app.getCreatedAt());
        res.setUpdatedAt(app.getUpdatedAt());
        return res;
    }

    public JobApplicationResponse create(String email, JobApplicationDto dto) {
        User user = getUser(email);

        JobApplication app = JobApplication.builder()
                .companyName(dto.getCompanyName())
                .roleName(dto.getRoleName())
                .jobDescription(dto.getJobDescription())
                .jobUrl(dto.getJobUrl())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .appliedDate(dto.getAppliedDate())
                .followUpDate(dto.getFollowUpDate())
                .notes(dto.getNotes())
                .user(user)
                .build();

        return toResponse(jobApplicationRepository.save(app));
    }

    public List<JobApplicationResponse> getAll(String email) {
        User user = getUser(email);
        return jobApplicationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobApplicationResponse> getByStatus(String email, ApplicationStatus status) {
        User user = getUser(email);
        return jobApplicationRepository
                .findByUserIdAndStatus(user.getId(), status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public JobApplicationResponse getById(String email, Long id) {
        User user = getUser(email);
        JobApplication app = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!app.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        return toResponse(app);
    }

    public JobApplicationResponse update(String email, Long id, JobApplicationDto dto) {
        User user = getUser(email);
        JobApplication app = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!app.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        app.setCompanyName(dto.getCompanyName());
        app.setRoleName(dto.getRoleName());
        app.setJobDescription(dto.getJobDescription());
        app.setJobUrl(dto.getJobUrl());
        app.setStatus(dto.getStatus());
        app.setPriority(dto.getPriority());
        app.setAppliedDate(dto.getAppliedDate());
        app.setFollowUpDate(dto.getFollowUpDate());
        app.setNotes(dto.getNotes());

        return toResponse(jobApplicationRepository.save(app));
    }

    public void delete(String email, Long id) {
        User user = getUser(email);
        JobApplication app = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!app.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        jobApplicationRepository.delete(app);
    }

    public DashboardStats getStats(String email) {
        User user = getUser(email);
        Long userId = user.getId();

        return new DashboardStats(
                jobApplicationRepository.countByUserId(userId),
                jobApplicationRepository.countByUserIdAndStatus(userId, ApplicationStatus.APPLIED),
                jobApplicationRepository.countByUserIdAndStatus(userId, ApplicationStatus.SHORTLISTED),
                jobApplicationRepository.countByUserIdAndStatus(userId, ApplicationStatus.INTERVIEW),
                jobApplicationRepository.countByUserIdAndStatus(userId, ApplicationStatus.OFFER),
                jobApplicationRepository.countByUserIdAndStatus(userId, ApplicationStatus.REJECTED)
        );
    }
}
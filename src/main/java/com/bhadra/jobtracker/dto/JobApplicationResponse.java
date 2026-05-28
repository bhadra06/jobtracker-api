package com.bhadra.jobtracker.dto;

import com.bhadra.jobtracker.entity.ApplicationStatus;
import com.bhadra.jobtracker.entity.Priority;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class JobApplicationResponse {
    private Long id;
    private String companyName;
    private String roleName;
    private String jobDescription;
    private String jobUrl;
    private ApplicationStatus status;
    private Priority priority;
    private LocalDate appliedDate;
    private LocalDate followUpDate;
    private String notes;
    private Integer aiMatchScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.bhadra.jobtracker.dto;

import com.bhadra.jobtracker.entity.ApplicationStatus;
import com.bhadra.jobtracker.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class JobApplicationDto {

    @NotBlank
    private String companyName;

    @NotBlank
    private String roleName;

    private String jobDescription;

    private String jobUrl;

    @NotNull
    private ApplicationStatus status;

    private Priority priority;

    private LocalDate appliedDate;

    private LocalDate followUpDate;

    private String notes;
}
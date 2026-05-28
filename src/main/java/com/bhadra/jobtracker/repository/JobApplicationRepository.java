package com.bhadra.jobtracker.repository;

import com.bhadra.jobtracker.entity.ApplicationStatus;
import com.bhadra.jobtracker.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUserId(Long userId);

    List<JobApplication> findByUserIdAndStatus(Long userId, ApplicationStatus status);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, ApplicationStatus status);

    @Query("SELECT j FROM JobApplication j WHERE j.user.id = :userId ORDER BY j.createdAt DESC")
    List<JobApplication> findByUserIdOrderByCreatedAtDesc(Long userId);
}
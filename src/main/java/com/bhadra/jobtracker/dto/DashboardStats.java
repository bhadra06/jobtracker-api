package com.bhadra.jobtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStats {
    private long total;
    private long applied;
    private long shortlisted;
    private long interview;
    private long offer;
    private long rejected;
}
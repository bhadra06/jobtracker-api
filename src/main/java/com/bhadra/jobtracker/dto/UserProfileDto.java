package com.bhadra.jobtracker.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserProfileDto {
    private String name;
    private List<String> skills;
}
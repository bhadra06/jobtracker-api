package com.bhadra.jobtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private String name;
    private String email;
    private List<String> skills;
}
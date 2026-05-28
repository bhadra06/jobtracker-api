package com.bhadra.jobtracker.service;

import com.bhadra.jobtracker.dto.UserProfileDto;
import com.bhadra.jobtracker.dto.UserProfileResponse;
import com.bhadra.jobtracker.entity.User;
import com.bhadra.jobtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserProfileResponse(user.getName(), user.getEmail(), user.getSkills());
    }

    public UserProfileResponse updateProfile(String email, UserProfileDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }
        if (dto.getSkills() != null) {
            user.setSkills(dto.getSkills());
        }

        userRepository.save(user);
        return new UserProfileResponse(user.getName(), user.getEmail(), user.getSkills());
    }
}
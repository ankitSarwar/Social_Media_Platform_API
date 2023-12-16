package com.example.social.media.platform.API.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Additional fields for user profile
    private String bio;

    // You can store the profile picture URL or binary data based on your requirement
    private String profilePictureUrl;

    // Other user-related fields (username, email, etc.) can be added here

    // Bi-directional OneToOne mapping with User entity
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
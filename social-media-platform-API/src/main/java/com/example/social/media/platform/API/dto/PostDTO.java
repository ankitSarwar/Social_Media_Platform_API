package com.example.social.media.platform.API.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long postId;
    private String content;
    private UserDTO user;

    // Constructors, getters, and setters
}
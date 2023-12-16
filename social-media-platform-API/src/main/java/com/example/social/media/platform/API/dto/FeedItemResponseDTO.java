package com.example.social.media.platform.API.dto;

import com.example.social.media.platform.API.model.Comment;
import com.example.social.media.platform.API.model.PostLike;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedItemResponseDTO {
    private Long postId;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private boolean isBlueTicked;
    private String postText;
    private String postImageUrl;
    private String postVideoUrl;
    private LocalDateTime postCreatedDate;
    private String postLocation;
    private List<PostLike> postLikes;
    private List<Comment> comments;


}
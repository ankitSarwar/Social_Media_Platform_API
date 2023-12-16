package com.example.social.media.platform.API.repository;

import com.example.social.media.platform.API.model.Comment;
import com.example.social.media.platform.API.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}

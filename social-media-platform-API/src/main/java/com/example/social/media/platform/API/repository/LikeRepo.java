package com.example.social.media.platform.API.repository;

import com.example.social.media.platform.API.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepo extends JpaRepository<PostLike, Long> {

    long countByPost_Id(Long postId);
}

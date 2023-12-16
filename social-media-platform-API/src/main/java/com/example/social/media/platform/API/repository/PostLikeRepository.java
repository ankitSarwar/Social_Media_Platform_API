package com.example.social.media.platform.API.repository;

import com.example.social.media.platform.API.model.Post;
import com.example.social.media.platform.API.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {



}

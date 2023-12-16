package com.example.social.media.platform.API.service;


import com.example.social.media.platform.API.model.PostLike;
import com.example.social.media.platform.API.repository.LikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    LikeRepo likeRepo;

    public void like(PostLike postLike) {
        likeRepo.save(postLike);
    }

    public long getLikes(Long postId) {

        return likeRepo.countByPost_Id(postId);
    }

}
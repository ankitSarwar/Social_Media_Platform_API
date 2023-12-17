package com.example.social.media.platform.API.repository;

import com.example.social.media.platform.API.model.Post;
import com.example.social.media.platform.API.model.PrivacySetting;
import com.example.social.media.platform.API.model.User;
import com.example.social.media.platform.API.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {


    List<Post> findByUser(User user);

    List<Post> findByUserIn(List<User> users);


    @Query("SELECT COUNT(u) FROM Post p JOIN p.repostedByUsers u WHERE p.id = :postId")
    long countSharesByPostId(@Param("postId") Long postId);
}

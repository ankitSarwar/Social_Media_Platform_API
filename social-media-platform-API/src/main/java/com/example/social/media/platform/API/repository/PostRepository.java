package com.example.social.media.platform.API.repository;

import com.example.social.media.platform.API.model.Post;
import com.example.social.media.platform.API.model.PrivacySetting;
import com.example.social.media.platform.API.model.User;
import com.example.social.media.platform.API.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {


    List<Post> findByUser(User user);

    List<Post> findByUserIn(List<User> users);


}

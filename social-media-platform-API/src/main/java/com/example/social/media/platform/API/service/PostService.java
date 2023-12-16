package com.example.social.media.platform.API.service;

import com.example.social.media.platform.API.exception.UserNotFoundException;
import com.example.social.media.platform.API.model.*;
import com.example.social.media.platform.API.repository.FriendshipRepository;
import com.example.social.media.platform.API.repository.PostRepository;
import com.example.social.media.platform.API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {


    @Autowired
    PostRepository postRepository;

    @Autowired
    UserService userService;

    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    UserRepository userRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public void createPost(Post post) {

        postRepository.save(post);
    }

    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }

    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    public void save(Post repostPost) {
        postRepository.save(repostPost);
    }

//    public List<Post> getAllPublicPosts() {
//        return postRepository.findByPrivacySetting(PrivacySetting.PUBLIC);
//    }


    public List<Post> getVisiblePosts(String userName) {
        // Get the user by username
        User user = userService.getUserByUsername(userName);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        // Retrieve and filter posts based on privacy settings
        return postRepository.findByUser(user)
                .stream()
                .filter(post -> isPostVisible(post, user))
                .collect(Collectors.toList());
    }




    private boolean isPostVisible(Post post, User currentUser) {
        // Check if the post is public or if it's visible to friends only
        if (post.getPrivacySetting() == PrivacySetting.PUBLIC) {
            return true;
        } else if (post.getPrivacySetting() == PrivacySetting.PRIVATE) {
            // Check if the post is visible to friends
            return isPostVisibleToFriends(post, currentUser);
        }

        return false;
    }

    private boolean isPostVisibleToFriends(Post post, User currentUser) {
        // Check if there is a Friendship with the current user and the post owner
        Optional<Friendship> friendship = friendshipRepository.findByUserAndFriend(currentUser, post.getUser());

        // Check if the Friendship exists and is accepted
        return friendship.isPresent() && friendship.get().getStatus() == FriendshipStatus.ACCEPTED;
    }



}

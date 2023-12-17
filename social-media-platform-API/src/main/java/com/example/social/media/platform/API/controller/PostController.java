package com.example.social.media.platform.API.controller;

import com.example.social.media.platform.API.exception.UserNotFoundException;
import com.example.social.media.platform.API.filter.JwtAuthFilter;
import com.example.social.media.platform.API.model.Post;
import com.example.social.media.platform.API.model.PostLike;
import com.example.social.media.platform.API.model.PrivacySetting;
import com.example.social.media.platform.API.model.User;
import com.example.social.media.platform.API.service.JwtService;
import com.example.social.media.platform.API.service.LikeService;
import com.example.social.media.platform.API.service.PostService;
import com.example.social.media.platform.API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Autowired
    LikeService likeService;

    @PostMapping("/create")
    public ResponseEntity<String> addPost(@RequestParam String username, @RequestParam String token, @RequestBody Post post) {
        // Validate the token
        if (jwtService.validateToken(token,username)) {
            // Get user information based on the provided username
          User currentUser = userService.getUserByUsername(username);

            if (currentUser != null) {
                // Set the user and createdDate for the post
                post.setUser(currentUser);
                post.setCreatedDate(LocalDateTime.now());

                // Save the post
                postService.createPost(post);

                return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token and UserName");
        }
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long postId,
            @RequestParam String username,
            @RequestParam String token
    ) {
        try {
            // Validate the token and get the user details
            String username1=jwtService.extractUsername(token);
            if (username1!=null) {
                // Get the post by ID
                Optional<Post> optionalPost = postService.getPostById(postId);

                if (optionalPost.isPresent()) {
                    // Check if the logged-in user is the owner of the post
                    User loggedInUser = userService.getUserByUsername(username1);//
                    Post post = optionalPost.get();

                    if (post.getUser().getId().equals(loggedInUser.getId())) {
                        // Delete the post
                        postService.deletePostById(postId);
                        return ResponseEntity.ok("Post deleted successfully.");
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this post.");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
                }
            }
        else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting the post.");
        }
    }


    @PostMapping("/repost/{postId}")
    public ResponseEntity<String> repostPost(
            @PathVariable Long postId,
            @RequestParam String token,
            @RequestBody Post repostedPost) {

        // Get the current user by token
        String username=jwtService.extractUsername(token);

        User currentUser = userService.getUserByUsername(username);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
        }

        // Get the post to be reposted by postId
        Optional<Post> optionalOriginalPost = postService.findById(postId);
        if (optionalOriginalPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Original post not found");
        }
        Post originalPost = optionalOriginalPost.get();

        // Check if the post is already reposted by the user
        if (currentUser.getRepostedPosts().contains(originalPost)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post already reposted");
        }

        // Create a new Post entity for the repost
        Post repostPost = new Post();

        repostPost.setUser(currentUser);
        repostPost.setText(repostedPost.getText()); // Use the provided text for the repost
        repostPost.setImageUrl(repostedPost.getImageUrl()); // Use the provided image URL for the repost
        repostPost.setVideoUrl(repostedPost.getVideoUrl()); // Use the provided video URL for the repost
        repostPost.setCreatedDate(LocalDateTime.now());
        repostPost.setLocation(repostedPost.getLocation()); // Use the provided location for the repost
        repostPost.setPrivacySetting(PrivacySetting.PUBLIC);
        // Save the reposted post
        postService.save(repostPost);

        // Update the user's repostedPosts collection
        currentUser.getRepostedPosts().add(repostPost);

        // Save the changes
        userService.save(currentUser);

        return ResponseEntity.ok("Repost successfully.");
    }



    @GetMapping("/public")
    public List<Post> getPublicPosts(@RequestParam String userName) {

        return postService.getVisiblePosts(userName);
    }

    @GetMapping("/private")
    public List<Post> getVisiblePosts(@RequestParam String token) {
        String username = jwtService.extractUsername(token);

        User currentUser = userService.getUserByUsername(username);
        if (currentUser == null) {
            throw new UserNotFoundException("User not authorized");
        }
        return postService.getVisiblePosts(username);
    }



    @PostMapping("/like")
    public ResponseEntity<String> likePost(@RequestBody PostLike postLike, @RequestParam String token) {
        // Validate token and extract user details
        String username = jwtService.extractUsername(token);
        Optional<User> tokenUserOptional = userService.findByUsername(username);

        if (tokenUserOptional.isPresent()) {
            User tokenUser = tokenUserOptional.get();

            // Check if the user ID from the token matches the user ID in the PostLike
            if (tokenUser.getId().equals(postLike.getUser().getId())) {
                likeService.like(postLike);
                return ResponseEntity.ok("Post liked successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token or user mismatch.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user not found.");
        }
    }

    @GetMapping("/{postId}/likeCount")
    public long getLikesForPost(@PathVariable Long postId)
    {
        return likeService.getLikes(postId);
    }


    @GetMapping("/getCountOfSharesPostsById/{postId}")
    public ResponseEntity<Long> getCountOfSharesPostsById(@PathVariable Long postId) {
        long shareCount = postService.getShareCountByPostId(postId);
        return ResponseEntity.ok(shareCount);
    }
}

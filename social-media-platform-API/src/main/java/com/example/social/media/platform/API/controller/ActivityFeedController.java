package com.example.social.media.platform.API.controller;

import com.example.social.media.platform.API.dto.FeedItem;
import com.example.social.media.platform.API.dto.FeedItemResponseDTO;
import com.example.social.media.platform.API.model.Friendship;
import com.example.social.media.platform.API.model.Post;
import com.example.social.media.platform.API.model.User;
import com.example.social.media.platform.API.service.ActivityFeedService;
import com.example.social.media.platform.API.service.FriendshipService;
import com.example.social.media.platform.API.service.JwtService;
import com.example.social.media.platform.API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activity-feed")
public class ActivityFeedController {

    @Autowired
    private ActivityFeedService activityFeedService;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @Autowired
    FriendshipService friendshipService;

    @GetMapping("/personalized")
    public ResponseEntity<List<Post>> getPersonalizedActivityFeed(
            @RequestParam Long userId,
            @RequestParam String token
    ) {
        String username = jwtService.extractUsername(token);
        Optional<User> currentUserOptional = userService.findByUsername(username);

        if (currentUserOptional.isPresent()) {
            User currentUser = currentUserOptional.get();

            // Check if the authenticated user's ID matches the provided userId
            if (currentUser.getId().equals(userId)) {
                // Proceed with retrieving the personalized activity feed
                List<Post> activityFeed = activityFeedService.getPersonalizedActivityFeed(userId);
                return ResponseEntity.ok(activityFeed);
            } else {
                // If the userId does not match the authenticated user's ID, return a forbidden response
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            // If the user is not found, return an unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/getNotification/{userId}")
    public ResponseEntity<List<FeedItemResponseDTO>> getPersonalizedActivityFeed1(@PathVariable Long userId) {
        List<FeedItemResponseDTO> activityFeed = activityFeedService.getPersonalActivityFeed1(userId);
        return ResponseEntity.ok(activityFeed);
    }


    @GetMapping("/checkFriendRequest")
    public ResponseEntity<String> checkFriendRequest(@RequestParam String token) {
        // Use the token to extract user details
        String username = jwtService.extractUsername(token);
        Optional<User> currentUserOptional = userService.findByUsername(username);

        if (currentUserOptional.isPresent()) {
            User currentUser = currentUserOptional.get();
            Long myId = currentUser.getId();

            // Check if there are pending friend requests received by the user
            List<Friendship> pendingFriendships = friendshipService.getPendingFriendRequest(myId);

            if (!pendingFriendships.isEmpty()) {
                // Create links and retrieve names for each pending friend request
                StringBuilder response = new StringBuilder();
                for (Friendship friendship : pendingFriendships) {
                    Long otherId = friendship.getUser().getId();
                    String otherName = friendship.getUser().getName();
                    String link1 = "http://localhost:8080/friendship/acceptFriendRequest/" + myId + "/" + otherId+"?token="+token;
                    String link2 = "http://localhost:8080/friendship/rejectFriendRequest/" + myId + "/" + otherId+"?token="+token;

                    response.append("Pending friend request from ").append(otherName).append(": \n").append("Accect request")
                            .append(": \n").append(link1).append("\n").append("Reject request").append(": \n")
                            .append(link2).append("\n\n");
                }

                return ResponseEntity.status(HttpStatus.OK).body(response.toString());
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No pending friend requests.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }



}
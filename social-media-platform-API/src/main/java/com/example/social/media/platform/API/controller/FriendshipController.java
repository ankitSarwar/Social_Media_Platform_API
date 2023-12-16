package com.example.social.media.platform.API.controller;

import com.example.social.media.platform.API.filter.JwtAuthFilter;
import com.example.social.media.platform.API.model.Friendship;
import com.example.social.media.platform.API.model.User;
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
@RequestMapping("/friendship")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    JwtAuthFilter jwtAuthFilter;



    @PostMapping("/sendFriendRequest/{myId}/{otherId}")
    public String sendFriendRequest(@PathVariable Long myId, @PathVariable Long otherId, @RequestParam String token) {
        // Validate the token
        Optional<User> myUserOptional = userService.getUserById(myId);

        if (myUserOptional.isPresent()) {
            User myUser = myUserOptional.get();

            // Use the token to extract user details
            String username= jwtService.extractUsername(token);
            Optional<User> tokenUser1=userService.findByUsername(username);
            User tokenUser=tokenUser1.orElse(null);
            // Check if the user ID in the token matches the provided myId
            if (myUser.getId().equals(tokenUser.getId())) {
                // Token is valid and matches the provided user ID, proceed with following the user
                return friendshipService.sendFriendRequest(myId, otherId);
            } else {
                // Token does not match the provided user ID
                return "Invalid token for the specified user";
            }
            // Send friend request
        } else {
            return "Invalid token for the specified user";
        }
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

    @PostMapping("/acceptFriendRequest/{myId}/{otherId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long myId, @PathVariable Long otherId,@RequestParam String token) {
        String result = friendshipService.acceptFriendRequest(myId, otherId,token);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/rejectFriendRequest/{myId}/{otherId}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable Long myId, @PathVariable Long otherId,@RequestParam String token) {
        String result = friendshipService.rejectFriendRequest(myId, otherId,token);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
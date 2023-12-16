package com.example.social.media.platform.API.service;

import com.example.social.media.platform.API.model.Friendship;
import com.example.social.media.platform.API.model.FriendshipStatus;
import com.example.social.media.platform.API.model.User;
import com.example.social.media.platform.API.repository.FriendshipRepository;
import com.example.social.media.platform.API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;



    public String sendFriendRequest(Long myId, Long otherId) {
        // Check if the friendship already exists
        if (friendshipRepository.existsByUserIdAndFriendIdAndStatus(myId, otherId, FriendshipStatus.PENDING) ||
                friendshipRepository.existsByUserIdAndFriendIdAndStatus(otherId, myId, FriendshipStatus.PENDING) ||
                friendshipRepository.existsByUserIdAndFriendIdAndStatus(myId, otherId, FriendshipStatus.ACCEPTED)) {
            return "Friendship already exists or request is pending.";
        }

        User myUser = new User();
        myUser.setId(myId);

        User otherUser = new User();
        otherUser.setId(otherId);

        // Create a new friendship request
        Friendship friendship = new Friendship();
        friendship.setUser(new User(myId));
        friendship.setFriend(new User(otherId));
        friendship.setStatus(FriendshipStatus.PENDING);

        friendshipRepository.save(friendship);
        return "Friend request sent successfully.";
    }

    public List<Friendship> getPendingFriendRequest(Long myId) {
        User currentUser = new User(myId);
        return friendshipRepository.findByFriendAndStatus(currentUser, FriendshipStatus.PENDING);
    }

    public String acceptFriendRequest(Long myId, Long otherId, String mytoken) {
        String username = jwtService.extractUsername(mytoken);
        Optional<User> currentUserOptional = userService.findByUsername(username);

        if (currentUserOptional.isPresent()) {
            User currentUser = currentUserOptional.get();
            // Check if the authenticated user's ID matches the provided myId
            if (currentUser.getId().equals(myId)) {
                // Continue with friend request acceptance logic
                Optional<Friendship> optionalFriendship = friendshipRepository.findByUserIdAndFriendIdAndStatus(otherId, myId, FriendshipStatus.PENDING);

                if (optionalFriendship.isPresent()) {
                    // Update the status to ACCEPTED
                    Friendship friendship = optionalFriendship.get();
                    friendship.setStatus(FriendshipStatus.ACCEPTED);
                    friendship.setFollowing(true);
                    friendshipRepository.save(friendship);
                    return "Friend request accepted.";
                } else {
                    return "Friend request not found or already accepted.";
                }
            } else {
                // User ID from token doesn't match the provided myId
                return "Invalid user ID.";
            }
        } else {
            // Invalid token
            return "Invalid token.";
        }
    }


    public String rejectFriendRequest(Long myId, Long otherId, String mytoken) {
        String username = jwtService.extractUsername(mytoken);
        Optional<User> currentUserOptional = userService.findByUsername(username);

        if (currentUserOptional.isPresent()) {
            User currentUser = currentUserOptional.get();
            // Check if the authenticated user's ID matches the provided myId
            if (currentUser.getId().equals(myId)) {
                // Continue with friend request rejection logic
                Optional<Friendship> optionalFriendship = friendshipRepository.findByUserIdAndFriendIdAndStatus(otherId, myId, FriendshipStatus.PENDING);

                if (optionalFriendship.isPresent()) {
                    // Remove the friend request
                    Friendship friendship = optionalFriendship.get();
                    friendshipRepository.delete(friendship);
                    return "Friend request rejected.";
                } else {
                    return "Friend request not found or already rejected.";
                }
            } else {
                // User ID from token doesn't match the provided myId
                return "Invalid user ID.";
            }
        } else {
            // Invalid token
            return "Invalid token.";
        }
    }


    public List<User> getFollowers(Long userId) {
        // Implement logic to get followers for a user
        // You may need to query the FriendshipRepository for followers based on the user ID

        return Collections.emptyList(); // Placeholder, replace with actual implementation
    }

    public List<User> getFollowing(Long userId) {
        // Implement logic to get users the specified user is following
        // You may need to query the FriendshipRepository for following based on the user ID

        return Collections.emptyList(); // Placeholder, replace with actual implementation
    }


}

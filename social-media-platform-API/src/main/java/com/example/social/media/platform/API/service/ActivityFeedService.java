package com.example.social.media.platform.API.service;

import com.example.social.media.platform.API.dto.FeedItem;
import com.example.social.media.platform.API.dto.FeedItemResponseDTO;
import com.example.social.media.platform.API.model.*;
import com.example.social.media.platform.API.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ActivityFeedService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    public List<Post> getPersonalizedActivityFeed(Long userId) {
        // Retrieve friends and followed users
        List<Friendship> friendships = friendshipRepository.findByUserOrFriend(new User(userId), new User(userId));
        Set<User> friendsAndFollowedUsers = friendships.stream()
                .flatMap(friendship -> Stream.of(friendship.getUser(), friendship.getFriend()))
                .collect(Collectors.toSet());

        // Retrieve posts from friends and followed users
        List<Post> postsFromFriendsAndFollowedUsers = postRepository.findByUserIn(new ArrayList<>(friendsAndFollowedUsers));

        // Sort the posts by createdDate in descending order
        postsFromFriendsAndFollowedUsers.sort(Comparator.comparing(Post::getCreatedDate).reversed());

        return postsFromFriendsAndFollowedUsers;
    }





    public List<FeedItemResponseDTO> getPersonalActivityFeed1(Long userId) {
        // Your existing logic to fetch posts, likes, and comments
        List<Post> postsFromFriendsAndFollowedUsers = getPersonalizedActivityFeed(userId);

        List<FeedItemResponseDTO> feedItems = new ArrayList<>();

        for (Post post : postsFromFriendsAndFollowedUsers) {
            // Fetch user details from the post
            User user = post.getUser();

            FeedItemResponseDTO feedItemDTO = new FeedItemResponseDTO();
            feedItemDTO.setPostId(post.getId());
            feedItemDTO.setUsername(user.getUsername());
            feedItemDTO.setName(user.getName());
            feedItemDTO.setEmail(user.getEmail());
            feedItemDTO.setPhoneNumber(user.getPhoneNumber());
            feedItemDTO.setBlueTicked(user.isBlueTicked());

            // Set post details
            feedItemDTO.setPostText(post.getText());
            feedItemDTO.setPostImageUrl(post.getImageUrl());
            feedItemDTO.setPostVideoUrl(post.getVideoUrl());
            feedItemDTO.setPostCreatedDate(post.getCreatedDate());
            feedItemDTO.setPostLocation(post.getLocation());

            // Set post likes and comments (you need to fetch them)

            feedItems.add(feedItemDTO);
        }
        return feedItems;
    }


}
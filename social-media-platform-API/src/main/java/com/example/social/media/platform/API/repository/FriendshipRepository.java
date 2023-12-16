package com.example.social.media.platform.API.repository;

import com.example.social.media.platform.API.model.Friendship;
import com.example.social.media.platform.API.model.FriendshipStatus;
import com.example.social.media.platform.API.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // Check if a friendship exists by user IDs and status
    boolean existsByUserIdAndFriendIdAndStatus(Long userId, Long friendId, FriendshipStatus status);

    // Check if a friendship exists by user IDs and following status
    boolean existsByUserIdAndFriendIdAndIsFollowing(Long userId, Long friendId, boolean isFollowing);

    // Find friendships by user ID and status
    List<Friendship> findByUserIdAndStatus(Long userId, FriendshipStatus status);

    // Find friendships by friend ID and status
    List<Friendship> findByFriendIdAndStatus(Long friendId, FriendshipStatus status);

    // Find friendships by user ID, friend ID, and status
    Optional<Friendship> findByUserIdAndFriendIdAndStatus(Long userId, Long friendId, FriendshipStatus status);

    // Find friendships by user ID and following status
    List<Friendship> findByUserIdAndIsFollowing(Long userId, boolean isFollowing);

    // Find friendships by friend ID and following status
    List<Friendship> findByFriendIdAndIsFollowing(Long friendId, boolean isFollowing);

    // Find friendships by user ID, friend ID, and following status
    Optional<Friendship> findByUserIdAndFriendIdAndIsFollowing(Long userId, Long friendId, boolean isFollowing);

    // Find followers by user ID and status
    List<Friendship> findByFriendIdAndStatusAndIsFollowing(Long friendId, FriendshipStatus status, boolean isFollowing);

    // Find following by user ID and status
    List<Friendship> findByUserIdAndStatusAndIsFollowing(Long userId, FriendshipStatus status, boolean isFollowing);


    List<Friendship> findByFriendAndStatus(User currentUser, FriendshipStatus friendshipStatus);


    Optional<Friendship> findByUserAndFriend(User currentUser, User user);

//    List<User> findByIsFollowing(Long userId);

//    List<User> findByStatusAndIsFollowing(FriendshipStatus friendshipStatus, boolean b);

    @Query("SELECT f.user FROM Friendship f WHERE f.status = :status AND f.isFollowing = :isFollowing")
    List<User> findByStatusAndIsFollowingUsers(@Param("status") FriendshipStatus status, @Param("isFollowing") boolean isFollowing);

    List<User> findAllByIsFollowing(boolean isFollowing);

    List<Friendship> findByUserOrFriend(User user, User friend);

    List<User> findByStatusAndIsFollowing(FriendshipStatus friendshipStatus, boolean b);


    // Modify the query as per your requirements
    @Query("SELECT DISTINCT f.user FROM Friendship f WHERE f.status = :status AND f.isFollowing = :isFollowing")
    List<User> findUsersByStatusAndIsFollowing(@Param("status") FriendshipStatus status, @Param("isFollowing") boolean isFollowing);
}
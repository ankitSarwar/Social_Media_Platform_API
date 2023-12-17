package com.example.social.media.platform.API.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Lob //  It stands for "Large Object" and is typically used to store large amounts of data, such as text, images, or binary data.
    private String text;

    private String imageUrl;

    private String videoUrl;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    private String location;


    @ManyToMany(mappedBy = "repostedPosts")
    private Set<User> repostedByUsers = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private PrivacySetting privacySetting;

    @Override
    public int hashCode() {
        return Objects.hash(id, text, imageUrl, videoUrl, createdDate, location, privacySetting);
    }

}
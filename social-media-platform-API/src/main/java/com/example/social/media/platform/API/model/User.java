package com.example.social.media.platform.API.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String name;

    @Email
    @Column(unique = true)
    private String email;

    private String password;

    private boolean enabled; // Indicates whether the user's email is verified

    @Column(nullable = false)
    @Pattern(regexp = "\\d{2}-\\d{10}", message = "Phone number should be in the format XX-XXXXXXXXXX")
    private String phoneNumber;

    @Column(unique = true)
    private String verificationToken;

    @Enumerated(EnumType.STRING)
    private UserRoles roles;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean isBlueTicked;// this should not be exposed to user : Hint : DTO

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;


    @ManyToMany
    @JoinTable(
            name = "user_reposts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> repostedPosts = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Friendship> friendships;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    public User(Long id) {
        this.id = id;
    }
}
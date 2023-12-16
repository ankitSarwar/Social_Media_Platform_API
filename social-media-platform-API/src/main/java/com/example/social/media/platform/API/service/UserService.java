package com.example.social.media.platform.API.service;

import com.example.social.media.platform.API.exception.EmailSendingException;
import com.example.social.media.platform.API.exception.InvalidTokenException;
import com.example.social.media.platform.API.exception.RegistrationException;
import com.example.social.media.platform.API.exception.UserNotFoundException;
import com.example.social.media.platform.API.model.PostLike;
import com.example.social.media.platform.API.model.User;
import com.example.social.media.platform.API.model.UserProfile;
import com.example.social.media.platform.API.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService  {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    JwtService jwtService;


    @Autowired
    LikeService likeService;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${app.verify-base-url}") // Specify this property in your application.properties or application.yml
    private String verifyBaseUrl;

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RegistrationException("Username already exists");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RegistrationException("Email address is already registered");
        }

        // Generate verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);

        // Set default role as USER
//        user.setRoles(Collections.singleton(UserRoles.USER));

        // Hash and store the password securely
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user
        User registeredUser = userRepository.save(user);

        // Send verification email
        sendVerificationEmail(registeredUser);

        return registeredUser;
    }

    public User verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalStateException("Invalid verification token"));

//        user.setVerificationToken(null);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    private void sendVerificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(user.getEmail());
        message.setSubject("Verify Your Email Address");
        message.setText("To verify your email address, click the link below:\n\n"
                + verifyBaseUrl + "/api/users/verify?token=" + user.getVerificationToken());

        try {
            javaMailSender.send(message);
            System.out.println("Verification email sent successfully");
        } catch (MailException e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            throw new EmailSendingException("Failed to send verification email.", e);
        }
    }


    public Optional<User> getUserById(Long id) {
        Optional<User> byId = userRepository.findById(id);
        return byId;
    }

    @Transactional
    public void deleteVerificationTokenByUsername(String username) {
        userRepository.deleteVerificationTokenByUsername(username);
    }


    public boolean isValidUser(String email, String token) {
        // Retrieve the user by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Check if the user exists and the token is valid
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return jwtService.validateToken(token, user.getUsername());
        } else {
            return false;
        }
    }

    @Transactional
    public void updateUserProfile(String email, String token, User updatedUser) {
        // Validate the token
        if (jwtService.validateToken(token)) {
            // Find the user by email
            Optional<User> optionalUser = userRepository.findByEmail(email);

            optionalUser.ifPresent(existingUser -> {
                // Update the user properties
                existingUser.setName(updatedUser.getName());
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

                UserProfile userProfile = existingUser.getUserProfile();
                if (userProfile != null) {
                    userProfile.setBio(updatedUser.getUserProfile().getBio());
                    userProfile.setProfilePictureUrl(updatedUser.getUserProfile().getProfilePictureUrl());
                }

                // Save the updated user
                userRepository.save(existingUser);
            });
        } else {
            throw new InvalidTokenException("Invalid token");
        }
    }


    public void toggleBlueTick(String username, boolean blueTick) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setBlueTicked(blueTick);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found with username: " + username);
        }
    }

    public void deleteUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.delete(user);
        } else {
            throw new UserNotFoundException("User not found with username: " + username);
        }
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found with username: " + username));
    }

    public void save(User currentUser) {
        userRepository.save(currentUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }




    public List<User> getAllUser() {
        return userRepository.findAll();
    }



}

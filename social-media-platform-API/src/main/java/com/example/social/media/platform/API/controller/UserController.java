package com.example.social.media.platform.API.controller;

import com.example.social.media.platform.API.config.SecurityConfig;
import com.example.social.media.platform.API.dto.AuthRequest;
import com.example.social.media.platform.API.exception.RegistrationException;
import com.example.social.media.platform.API.model.PostLike;
import com.example.social.media.platform.API.model.User;
import com.example.social.media.platform.API.service.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    PostService postService;

    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    FriendshipService friendshipService;

    @Autowired
    LikeService likeService;

    @Value("${app.verify-base-url}") // Specify this property in your application.properties or application.yml
    private String verifyBaseUrl;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);

            // Construct the verification link
            String verificationLink = verifyBaseUrl + "/api/users/verify?token=" + registeredUser.getVerificationToken();

            // Send the verification link in the response
            return ResponseEntity.ok("User registered successfully. Please check your email for verification. Verification Link: " + verificationLink);
        } catch (RegistrationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Registration failed.");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        User verifiedUser = userService.verifyUser(token);
        return ResponseEntity.ok("User verified successfully. You can now log in.");
    }

    @PostMapping("/logIn")
    public String LogInUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
//            return "you are logIn..... done";
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @DeleteMapping("/logout")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> signOut(@RequestParam String username, HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails = securityConfig.userDetailsService().loadUserByUsername(username);

        if (userDetails != null) {
            // Perform any additional user-specific logout logic here

            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // Now, perform the logout
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return ResponseEntity.ok("User " + username + " signed out successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }


    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestParam String email, @RequestParam String token, @RequestBody User user) {
        try {
            // Validate the user based on the provided email and token
            if (userService.isValidUser(email, token)) {
                userService.updateUserProfile(email,token, user);
                return ResponseEntity.ok("User profile updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user profile.");
        }
    }








}
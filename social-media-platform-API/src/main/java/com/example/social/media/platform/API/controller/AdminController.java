package com.example.social.media.platform.API.controller;

import com.example.social.media.platform.API.exception.UserNotFoundException;
import com.example.social.media.platform.API.model.User;
import com.example.social.media.platform.API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {


    @Autowired
    UserService userService;

    @GetMapping("/{id}")  // only admin can get ALL user and Admin
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("BlueTickUpdate/{username}/{blueTick}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> toggleBlueTick(@PathVariable String username, @PathVariable boolean blueTick) {
        try {
            userService.toggleBlueTick(username, blueTick);
            return ResponseEntity.ok("Blue tick for user '" + username + "' toggled successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling blue tick.");
        }
    }

    @DeleteMapping("/delete/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@RequestParam String username) {
        try {
            userService.deleteUserByUsername(username);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user.");
        }
    }

}


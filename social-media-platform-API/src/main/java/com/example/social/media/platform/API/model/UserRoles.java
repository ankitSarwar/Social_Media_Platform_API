package com.example.social.media.platform.API.model;

import org.springframework.data.util.Streamable;

import java.util.stream.Stream;

public enum UserRoles {
    USER, ADMIN;


    public Streamable<Object> stream() {
        return Streamable.of(UserRoles.values());
    }
}
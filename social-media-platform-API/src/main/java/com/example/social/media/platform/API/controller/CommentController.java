package com.example.social.media.platform.API.controller;


import com.example.social.media.platform.API.model.Comment;
import com.example.social.media.platform.API.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    String addComment(@RequestBody Comment comment)
    {
        return commentService.addComment(comment);
    }



}

package com.example.social.media.platform.API.service;

import com.example.social.media.platform.API.model.Comment;
import com.example.social.media.platform.API.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepo;

    //definitely make use of authentication
    public String addComment(Comment comment) {
        Comment rComment = commentRepo.save(comment);
        if(rComment == null)
        {
            return "Comment not saved...!";
        }
        else
        {
            return "Comment saved...!";
        }
    }



}

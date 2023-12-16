package com.example.social.media.platform.API.dto;


import com.example.social.media.platform.API.model.Comment;
import com.example.social.media.platform.API.model.Post;
import com.example.social.media.platform.API.model.PostLike;
import lombok.Data;

import java.util.List;

@Data
public class FeedItem {
    private Post post;
    private List<PostLike> postLikes;
    private List<Comment> comments;

    public FeedItem(Post post, List<PostLike> postLikes, List<Comment> comments) {
        this.post = post;
        this.postLikes = postLikes;
        this.comments = comments;
    }
}
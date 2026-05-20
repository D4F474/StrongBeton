package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.FeedPostCommentDTO;
import com.strongBeton.strongBeton.DTO.FeedPostDTO;
import com.strongBeton.strongBeton.entity.User;

import java.util.List;

public interface FeedService {

    FeedPostDTO newPost(FeedPostDTO feedPostDTO, User user);
    boolean likePost(int postId, User user);
    FeedPostCommentDTO commentPost(int postId, FeedPostCommentDTO commentDTO, User user);
    List<FeedPostDTO> getFeed(User user);
    boolean deletePost(int postId, User user);
}

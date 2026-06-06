package com.strongBeton.strongBeton.service.social;

import com.strongBeton.strongBeton.dto.social.FeedPostCommentDTO;
import com.strongBeton.strongBeton.dto.social.FeedPostDTO;
import com.strongBeton.strongBeton.entity.user.User;

import java.util.List;

public interface FeedService {

    FeedPostDTO newPost(FeedPostDTO feedPostDTO, User user);
    boolean likePost(int postId, User user);
    FeedPostCommentDTO commentPost(int postId, FeedPostCommentDTO commentDTO, User user);
    List<FeedPostDTO> getFeed(User user);
    boolean deletePost(int postId, User user);
}

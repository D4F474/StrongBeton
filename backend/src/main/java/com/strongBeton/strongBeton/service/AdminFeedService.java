package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dto.AdminFeedPostDTO;
import com.strongBeton.strongBeton.dto.AdminFeedStatsDTO;
import com.strongBeton.strongBeton.dto.AdminFeedCommentDTO;

import java.util.List;

public interface AdminFeedService {
    AdminFeedStatsDTO getStats();

    List<AdminFeedPostDTO> getPosts(String search, String filter);

    List<AdminFeedCommentDTO> getComments(int postId);

    void togglePostHidden(int postId);

    void togglePostPinned(int postId);

    void toggleCommentsLocked(int postId);

    void deletePost(int postId);

    void toggleCommentHidden(int commentId);

    void deleteComment(int commentId);
}

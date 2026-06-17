package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dao.FeedPostCommentRepository;
import com.strongBeton.strongBeton.dao.FeedPostRepository;
import com.strongBeton.strongBeton.dto.AdminFeedCommentDTO;
import com.strongBeton.strongBeton.dto.AdminFeedPostDTO;
import com.strongBeton.strongBeton.dto.AdminFeedStatsDTO;
import com.strongBeton.strongBeton.entity.social.FeedPost;
import com.strongBeton.strongBeton.entity.social.FeedPostComment;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class AdminFeedServiceImpl implements AdminFeedService {

    private final FeedPostRepository feedPostRepository;
    private final FeedPostCommentRepository feedPostCommentRepository;

    public AdminFeedServiceImpl(FeedPostRepository feedPostRepository,
                                FeedPostCommentRepository feedPostCommentRepository) {
        this.feedPostRepository = feedPostRepository;
        this.feedPostCommentRepository = feedPostCommentRepository;
    }

    @Override
    @Transactional
    public AdminFeedStatsDTO getStats() {
        List<FeedPost> posts = feedPostRepository.findAll();

        AdminFeedStatsDTO dto = new AdminFeedStatsDTO();
        dto.setTotalPosts(posts.size());
        dto.setVisiblePosts(posts.stream().filter(post -> !post.isHidden()).count());
        dto.setHiddenPosts(posts.stream().filter(FeedPost::isHidden).count());
        dto.setPinnedPosts(posts.stream().filter(FeedPost::isPinned).count());
        dto.setReportedPosts(0);
        dto.setTotalComments(feedPostCommentRepository.count());

        return dto;
    }

    @Override
    @Transactional
    public List<AdminFeedPostDTO> getPosts(String search, String filter) {
        String normalizedSearch = normalizeSearch(search);
        String normalizedFilter = normalizeFilter(filter);

        return feedPostRepository.findAll()
                .stream()
                .filter(post -> matchesFilter(post, normalizedFilter))
                .filter(post -> matchesSearch(post, normalizedSearch))
                .sorted(Comparator
                        .comparing(FeedPost::isPinned).reversed()
                        .thenComparing(FeedPost::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::mapPostToDTO)
                .toList();
    }

    @Override
    @Transactional
    public List<AdminFeedCommentDTO> getComments(int postId) {
        FeedPost post = getPostOrThrow(postId);

        return post.getComments()
                .stream()
                .sorted(Comparator.comparing(FeedPostComment::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::mapCommentToDTO)
                .toList();
    }

    @Override
    @Transactional
    public void togglePostHidden(int postId) {
        FeedPost post = getPostOrThrow(postId);
        post.setHidden(!post.isHidden());
        post.setUpdatedAt(LocalDateTime.now());
        feedPostRepository.save(post);
    }

    @Override
    @Transactional
    public void togglePostPinned(int postId) {
        FeedPost post = getPostOrThrow(postId);
        post.setPinned(!post.isPinned());
        post.setUpdatedAt(LocalDateTime.now());
        feedPostRepository.save(post);
    }

    @Override
    @Transactional
    public void toggleCommentsLocked(int postId) {
        FeedPost post = getPostOrThrow(postId);
        post.setCommentsLocked(!post.isCommentsLocked());
        post.setUpdatedAt(LocalDateTime.now());
        feedPostRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(int postId) {
        FeedPost post = getPostOrThrow(postId);
        feedPostRepository.delete(post);
    }

    @Override
    @Transactional
    public void toggleCommentHidden(int commentId) {
        FeedPostComment comment = getCommentOrThrow(commentId);
        comment.setHidden(!comment.isHidden());
        comment.setUpdatedAt(LocalDateTime.now());
        feedPostCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(int commentId) {
        FeedPostComment comment = getCommentOrThrow(commentId);
        feedPostCommentRepository.delete(comment);
    }

    private FeedPost getPostOrThrow(int postId) {
        return feedPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    private FeedPostComment getCommentOrThrow(int commentId) {
        return feedPostCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }

    private AdminFeedPostDTO mapPostToDTO(FeedPost post) {
        AdminFeedPostDTO dto = new AdminFeedPostDTO();

        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setUsername(post.getUser() == null ? "Unknown" : post.getUser().getUsername());
        dto.setType(post.getPostType() == null ? null : post.getPostType().name());
        dto.setLikesCount(post.getLikes() == null ? 0 : post.getLikes().size());
        dto.setCommentsCount(post.getComments() == null ? 0 : post.getComments().size());
        dto.setReportsCount(0);
        dto.setHidden(post.isHidden());
        dto.setPinned(post.isPinned());
        dto.setCommentsLocked(post.isCommentsLocked());
        dto.setCreatedAt(post.getCreatedAt());

        return dto;
    }

    private AdminFeedCommentDTO mapCommentToDTO(FeedPostComment comment) {
        AdminFeedCommentDTO dto = new AdminFeedCommentDTO();

        dto.setId(comment.getId());
        dto.setPostId(comment.getFeedPost() == null ? 0 : comment.getFeedPost().getId());
        dto.setContent(comment.getContent());
        dto.setUsername(comment.getUser() == null ? "Unknown" : comment.getUser().getUsername());
        dto.setHidden(comment.isHidden());
        dto.setCreatedAt(comment.getCreatedAt());

        return dto;
    }

    private boolean matchesFilter(FeedPost post, String filter) {
        return switch (filter) {
            case "VISIBLE" -> !post.isHidden();
            case "HIDDEN" -> post.isHidden();
            case "PINNED" -> post.isPinned();
            case "REPORTED" -> false;
            default -> true;
        };
    }

    private boolean matchesSearch(FeedPost post, String search) {
        if (search.isBlank()) {
            return true;
        }

        String content = post.getContent() == null ? "" : post.getContent().toLowerCase(Locale.ROOT);
        String username = post.getUser() == null || post.getUser().getUsername() == null
                ? ""
                : post.getUser().getUsername().toLowerCase(Locale.ROOT);

        return content.contains(search) || username.contains(search);
    }

    private String normalizeSearch(String search) {
        return search == null ? "" : search.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeFilter(String filter) {
        return filter == null ? "ALL" : filter.trim().toUpperCase(Locale.ROOT);
    }
}

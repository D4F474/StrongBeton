package com.strongBeton.strongBeton.service.social;

import com.strongBeton.strongBeton.dto.social.FeedPostCommentDTO;
import com.strongBeton.strongBeton.dto.social.FeedPostDTO;
import com.strongBeton.strongBeton.dao.*;
import com.strongBeton.strongBeton.dto.user.ImageDataDTO;
import com.strongBeton.strongBeton.entity.social.FeedPost;
import com.strongBeton.strongBeton.entity.social.FeedPostComment;
import com.strongBeton.strongBeton.entity.social.FeedPostLike;
import com.strongBeton.strongBeton.entity.social.FriendView;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.enums.PostType;
import com.strongBeton.strongBeton.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService{

    private FeedPostRepository feedPostRepository;
    private FeedPostLikeRepository feedPostLikeRepository;
    private FeedPostCommentRepository feedPostCommentRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private FriendViewRepository friendViewRepository;
    private ImageService imageService;

    @Autowired
    public FeedServiceImpl(FeedPostRepository feedPostRepository, FeedPostLikeRepository feedPostLikeRepository,
                           FeedPostCommentRepository feedPostCommentRepository, UserRepository userRepository,
                           ModelMapper modelMapper, FriendViewRepository friendViewRepository, ImageService imageService) {
        this.feedPostRepository = feedPostRepository;
        this.feedPostLikeRepository = feedPostLikeRepository;
        this.feedPostCommentRepository = feedPostCommentRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.friendViewRepository = friendViewRepository;
        this.imageService = imageService;
    }


    @Override
    @Transactional
    public FeedPostDTO newPost(FeedPostDTO feedPostDTO, User user) {
        Optional<FeedPost> fd = feedPostDTO.getId() != null
                ? this.feedPostRepository.findById(feedPostDTO.getId())
                : Optional.empty();
        FeedPost feedPost;
        User managedUser = userRepository.findByUuid(user.getUuid()).orElseThrow();
        LocalDateTime now = LocalDateTime.now();

        if (fd.isEmpty()) {
            feedPost = new FeedPost();
            feedPost.setUser(managedUser);
            feedPost.setContent(feedPostDTO.getContent());
            feedPost.setPostType(PostType.valueOf(feedPostDTO.getType()));
            feedPost.setCreatedAt(now);
            feedPost.setUpdatedAt(now);
        } else {
            feedPost = fd.get();
            feedPost.setContent(feedPostDTO.getContent());
            feedPost.setUpdatedAt(now);
        }
        FeedPost savedPost = feedPostRepository.save(feedPost);
        return mapPostToDTO(savedPost, managedUser);
    }

    @Override
    @Transactional
    public boolean likePost(int postId, User user) {
        FeedPost feedPost = this.feedPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (feedPost.isHidden()) {
            throw new IllegalArgumentException("Post is hidden");
        }

        User managedUser = userRepository.findByUuid(user.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<FeedPostLike> existingLike = this.feedPostLikeRepository
                .findByFeedPostAndUser(feedPost, managedUser);

        if (existingLike.isPresent()) {
            this.feedPostLikeRepository.delete(existingLike.get());
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        FeedPostLike feedPostLike = new FeedPostLike();
        feedPostLike.setFeedPost(feedPost);
        feedPostLike.setUser(managedUser);
        feedPostLike.setCreatedAt(now);
        feedPostLike.setUpdatedAt(now);

        this.feedPostLikeRepository.save(feedPostLike);

        return true;
    }

    @Override
    @Transactional
    public FeedPostCommentDTO commentPost(int postId, FeedPostCommentDTO commentDTO, User user) {
        FeedPost feedPost = this.feedPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (feedPost.isHidden()) {
            throw new IllegalArgumentException("Post is hidden");
        }

        if (feedPost.isCommentsLocked()) {
            throw new IllegalArgumentException("Comments are locked for this post");
        }

        User managedUser = userRepository.findByUuid(user.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        LocalDateTime now = LocalDateTime.now();

        FeedPostComment feedPostComment = new FeedPostComment();
        feedPostComment.setFeedPost(feedPost);
        feedPostComment.setUser(managedUser);
        feedPostComment.setContent(commentDTO.getContent());
        feedPostComment.setCreatedAt(now);
        feedPostComment.setUpdatedAt(now);

        FeedPostComment savedComment = feedPostCommentRepository.save(feedPostComment);
        return mapCommentToDTO(savedComment);
    }

    @Override
    @Transactional
    public boolean deletePost(int postId, User user) {
        FeedPost feedPost = feedPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (feedPost.getUser().getId() != user.getId()) {
            throw new RuntimeException("You are not the owner of this post");
        }

        feedPostRepository.delete(feedPost);
        return true;
    }

    private FeedPostDTO mapPostToDTO(FeedPost post, User currentUser) {
        FeedPostDTO dto = new FeedPostDTO();

        dto.setId(post.getId());
        dto.setContent(post.getContent());
        Optional< ImageDataDTO> result =  imageService.getProfileImage(post.getUser().getId());
        if(result.isPresent()){
            dto.setProfilePhotoUrl(result.get().getPhotoUrl());
        }
        if (post.getPostType() != null) {
            dto.setType(post.getPostType().name());
        }

        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());

        if (post.getUser() != null) {
            dto.setUsername(post.getUser().getUsername());
            dto.setUserUuid(post.getUser().getUuid());
        }

        dto.setLikesCount(post.getLikes() == null ? 0 : post.getLikes().size());
        dto.setCommentsLocked(post.isCommentsLocked());

        List<FeedPostCommentDTO> comments = post.getComments() == null
                ? List.of()
                : post.getComments()
                .stream()
                .filter(comment -> !comment.isHidden())
                .map(this::mapCommentToDTO)
                .toList();

        dto.setCommentsCount(comments.size());

        boolean likedByMe = post.getLikes() != null &&
                post.getLikes()
                        .stream()
                        .anyMatch(like -> like.getUser() != null &&
                                like.getUser().getId() == currentUser.getId());

        dto.setLikedByMe(likedByMe);

        dto.setComments(comments);

        return dto;
    }

    private FeedPostCommentDTO mapCommentToDTO(FeedPostComment comment) {
        FeedPostCommentDTO dto = new FeedPostCommentDTO();
        Optional< ImageDataDTO> result =  imageService.getProfileImage(comment.getUser().getId());
        if(result.isPresent()){
            dto.setProfilePhotoUrl(result.get().getPhotoUrl());
        }
            dto.setId(comment.getId());
            dto.setContent(comment.getContent());
            dto.setCreatedAt(comment.getCreatedAt());
            dto.setUpdatedAt(comment.getUpdatedAt());

        if (comment.getUser() != null) {
            dto.setUsername(comment.getUser().getUsername());
            dto.setUserUuid(comment.getUser().getUuid());
        }

        return dto;
    }

    @Override
    @Transactional
    public List<FeedPostDTO> getFeed(User user) {
        List<Integer> friendIds = new ArrayList<>();

        Optional<List<FriendView>> friendViews = this.friendViewRepository.findAllFriendsVisual(user.getUsername());

        if (friendViews.isPresent()) {
            List<String> friendUsernames = friendViews.get()
                    .stream()
                    .map(FriendView::getFriend)
                    .collect(Collectors.toList());

            friendIds = new ArrayList<>(userRepository.findIdsByUsername(friendUsernames));
        }

        friendIds.add(user.getId());

        return feedPostRepository.findByUserIdsOrderByCreatedAtDesc(friendIds)
                .stream()
                .map(post -> mapPostToDTO(post, user))
                .collect(Collectors.toList());
    }
}

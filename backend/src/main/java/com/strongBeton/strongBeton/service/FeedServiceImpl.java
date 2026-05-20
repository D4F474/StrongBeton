package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.FeedPostCommentDTO;
import com.strongBeton.strongBeton.DTO.FeedPostDTO;
import com.strongBeton.strongBeton.dao.*;
import com.strongBeton.strongBeton.entity.*;
import com.strongBeton.strongBeton.enums.PostType;
import com.strongBeton.strongBeton.mapper.ModelMapperConfig;
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

    @Autowired
    public FeedServiceImpl(FeedPostRepository feedPostRepository, FeedPostLikeRepository feedPostLikeRepository,
                           FeedPostCommentRepository feedPostCommentRepository, UserRepository userRepository,
                           ModelMapper modelMapper, FriendViewRepository friendViewRepository) {
        this.feedPostRepository = feedPostRepository;
        this.feedPostLikeRepository = feedPostLikeRepository;
        this.feedPostCommentRepository = feedPostCommentRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.friendViewRepository = friendViewRepository;
    }


    @Override
    @Transactional
    public FeedPostDTO newPost(FeedPostDTO feedPostDTO, User user) {
        Optional<FeedPost> fd = feedPostDTO.getId() != null
                ? this.feedPostRepository.findById(feedPostDTO.getId())
                : Optional.empty();
        FeedPost feedPost;
        User managedUser = userRepository.findByUuid(user.getUuid()).orElseThrow();

        if(fd.isEmpty()){
            feedPost = new FeedPost();
            feedPost.setUser(managedUser);
            feedPost.setContent(feedPostDTO.getContent());
            feedPost.setPostType(PostType.valueOf(feedPostDTO.getType()));
            feedPost.setCreatedAt(LocalDateTime.now());
        }else{
            feedPost = fd.get();
            feedPost.setContent(feedPostDTO.getContent());
            feedPost.setUpdatedAt(LocalDateTime.now());
        }
        return modelMapper.map(feedPostRepository.save(feedPost), FeedPostDTO.class);
    }

    @Override
    public boolean likePost(int postId, User user) {
        FeedPost feedPost = this.feedPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        User managedUser = userRepository.findByUuid(user.getUuid()).orElseThrow();

        Optional<FeedPostLike> existingLike = this.feedPostLikeRepository
                .findByFeedPostAndUser(feedPost, managedUser);

        if(existingLike.isPresent()){
            this.feedPostLikeRepository.delete(existingLike.get());
        } else {
            FeedPostLike feedPostLike = new FeedPostLike();
            feedPostLike.setFeedPost(feedPost);
            feedPostLike.setUser(managedUser);
            this.feedPostLikeRepository.save(feedPostLike);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public FeedPostCommentDTO commentPost(int postId, FeedPostCommentDTO commentDTO, User user) {
        FeedPostComment feedPostComment;
        FeedPost feedPost = this.feedPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        User managedUser = userRepository.findByUuid(user.getUuid()).orElseThrow();

            feedPostComment = new FeedPostComment();
            feedPostComment.setFeedPost(feedPost);
            feedPostComment.setUser(managedUser);
            feedPostComment.setContent(commentDTO.getContent());
            feedPostComment.setCreatedAt(LocalDateTime.now());

        return modelMapper.map(feedPostCommentRepository.save(feedPostComment), FeedPostCommentDTO.class);
    }

    @Override
    public List<FeedPostDTO> getFeed(User user) {

        List<Integer> friendIds = new ArrayList<>();

        Optional<List<FriendView>> friendViews = this.friendViewRepository.findAllFriendsVisual(user.getUsername());
        if(friendViews.isPresent()){
            List<String> friendUsernames = friendViews.get()
                    .stream()
                    .map(FriendView::getFriend)
                    .collect(Collectors.toList());
            friendIds = new ArrayList<>(userRepository.findIdsByUsername(friendUsernames));
        }

        friendIds.add(user.getId());

        return feedPostRepository.findByUserIdInOrderByCreatedAtDesc(friendIds)
                .stream()
                .map(post -> modelMapper.map(post, FeedPostDTO.class))
                .collect(Collectors.toList());
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
}

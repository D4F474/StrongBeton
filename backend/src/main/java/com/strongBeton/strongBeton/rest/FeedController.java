package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.social.FeedPostCommentDTO;
import com.strongBeton.strongBeton.dto.social.FeedPostDTO;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.service.social.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class FeedController {

    private FeedService feedService;

    @Autowired
    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/loadPosts")
    public ResponseEntity<?> loadPosts(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(this.feedService.getFeed(user));
    }

    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@RequestBody FeedPostDTO feedPostDTO, @AuthenticationPrincipal User user){
        return ResponseEntity.ok(this.feedService.newPost(feedPostDTO, user));
    }

    @PostMapping("/likePost/{postId}")
    public ResponseEntity<?> likePost(@PathVariable int postId, @AuthenticationPrincipal User user){
        return this.feedService.likePost(postId, user) ?
                ResponseEntity.ok("Post liked!") : ResponseEntity.ok("Post unliked!");
    }

    @PostMapping("/commentPost/{postId}")
    public ResponseEntity<?> commentPost(@PathVariable int postId,
                                         @RequestBody FeedPostCommentDTO commentDTO,
                                         @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(this.feedService.commentPost(postId, commentDTO, user));
    }

    @PutMapping("/updatePost")
    public ResponseEntity<?> updatePost(@RequestBody FeedPostDTO feedPostDTO, @AuthenticationPrincipal User user){
        return ResponseEntity.ok(this.feedService.newPost(feedPostDTO, user));
    }
    @DeleteMapping("/deletePost/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int postId, @AuthenticationPrincipal User user){
        return this.feedService.deletePost(postId, user) ?
                ResponseEntity.ok("Post deleted") :  ResponseEntity.ok("Cant delete post!");
    }
}

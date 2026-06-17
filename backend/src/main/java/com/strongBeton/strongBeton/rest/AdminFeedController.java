package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.service.AdminFeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/feed")
public class AdminFeedController {
    private final AdminFeedService adminFeedService;


    public AdminFeedController(AdminFeedService adminFeedService) {
        this.adminFeedService = adminFeedService;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(adminFeedService.getStats());
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "ALL") String filter
    ) {
        return ResponseEntity.ok(adminFeedService.getPosts(search, filter));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable int postId) {
        return ResponseEntity.ok(adminFeedService.getComments(postId));
    }

    @PatchMapping("/posts/{postId}/hidden")
    public ResponseEntity<?> togglePostHidden(@PathVariable int postId) {
        adminFeedService.togglePostHidden(postId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/posts/{postId}/pinned")
    public ResponseEntity<?> togglePostPinned(@PathVariable int postId) {
        adminFeedService.togglePostPinned(postId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/posts/{postId}/comments-locked")
    public ResponseEntity<?> toggleCommentsLocked(@PathVariable int postId) {
        adminFeedService.toggleCommentsLocked(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int postId) {
        adminFeedService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/comments/{commentId}/hidden")
    public ResponseEntity<?> toggleCommentHidden(@PathVariable int commentId) {
        adminFeedService.toggleCommentHidden(commentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId) {
        adminFeedService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

}

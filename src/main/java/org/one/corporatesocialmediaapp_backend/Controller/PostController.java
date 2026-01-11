package org.one.corporatesocialmediaapp_backend.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.DTO.*;
import org.one.corporatesocialmediaapp_backend.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    // ========== POST OPERATIONS ==========

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        PostResponse response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> createPostWithImage(
            @RequestParam("content") String content,
            @RequestParam("user_db_Id") Long userDbId,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        CreatePostRequest request = new CreatePostRequest(userDbId, content, null);
        PostResponse response = postService.createPostWithImage(request, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(
            @PathVariable Long postId,
            @RequestParam("currentUserId") Long currentUserId) {
        PostResponse response = postService.getPostById(postId, currentUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam("currentUserId") Long currentUserId) {
        List<PostResponse> response = postService.getAllPosts(currentUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam("currentUserId") Long currentUserId) {
        List<PostResponse> response = postService.getUserPosts(userId, currentUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getFeed(@RequestParam("currentUserId") Long currentUserId) {
        List<PostResponse> response = postService.getFeed(currentUserId);
        return ResponseEntity.ok(response);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> updatePost(
            @Valid @RequestBody UpdatePostRequest request,
            @RequestParam("currentUserId") Long currentUserId) {
        PostResponse response = postService.updatePost(request, currentUserId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestParam("currentUserId") Long currentUserId) {
        postService.deletePost(postId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    // ========== LIKE OPERATIONS ==========

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponse> likePost(
            @PathVariable Long postId,
            @RequestParam("currentUserId") Long currentUserId) {
        LikeResponse response = postService.likePost(postId, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> unlikePost(
            @PathVariable Long postId,
            @RequestParam("currentUserId") Long currentUserId) {
        postService.unlikePost(postId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<List<LikeResponse>> getPostLikes(@PathVariable Long postId) {
        List<LikeResponse> response = postService.getPostLikes(postId);
        return ResponseEntity.ok(response);
    }

    // ========== COMMENT OPERATIONS ==========

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestParam("currentUserId") Long currentUserId,
            @Valid @RequestBody CreateCommentRequest requestBody) {

        CreateCommentRequest request = requestBody;
        if (request.postId() == null || !request.postId().equals(postId)) {
            request = new CreateCommentRequest(request.content(), postId);
        }

        CommentResponse response = postService.createComment(request, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getPostComments(
            @PathVariable Long postId,
            @RequestParam("currentUserId") Long currentUserId) {
        List<CommentResponse> response = postService.getPostComments(postId, currentUserId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comments")
    public ResponseEntity<CommentResponse> updateComment(
            @Valid @RequestBody UpdateCommentRequest request,
            @RequestParam("currentUserId") Long currentUserId) {
        CommentResponse response = postService.updateComment(request, currentUserId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam("currentUserId") Long currentUserId) {
        postService.deleteComment(commentId, currentUserId);
        return ResponseEntity.noContent().build();
    }
}


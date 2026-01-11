package org.one.corporatesocialmediaapp_backend.Service;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.DTO.*;
import org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions.*;
import org.one.corporatesocialmediaapp_backend.Exceptions.LikeExceptions.LikeAlreadyExistsException;
import org.one.corporatesocialmediaapp_backend.Exceptions.LikeExceptions.LikeNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.PostExceptions.*;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserNotFoundException;
import org.one.corporatesocialmediaapp_backend.Mapper.DTOMapper;
import org.one.corporatesocialmediaapp_backend.Models.Comment;
import org.one.corporatesocialmediaapp_backend.Models.Like;
import org.one.corporatesocialmediaapp_backend.Models.Post;
import org.one.corporatesocialmediaapp_backend.Models.User;
import org.one.corporatesocialmediaapp_backend.Repositories.*;
import org.one.corporatesocialmediaapp_backend.Service.StorageService.ImageStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final DTOMapper dtoMapper;
    private final ImageStorage imageStorage;

    // ========== POST OPERATIONS ==========

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        if (request.content() == null || request.content().isBlank()) {
            throw new PostContentEmptyException("Post content cannot be empty");
        }

        User author = userRepository.findById(request.user_db_Id())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = dtoMapper.toPostEntity(request);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        return dtoMapper.toPostResponse(savedPost, author, List.of());
    }

    @Transactional
    public PostResponse createPostWithImage(CreatePostRequest request, MultipartFile imageFile) {
        if (request.content() == null || request.content().isBlank()) {
            throw new PostContentEmptyException("Post content cannot be empty");
        }

        User author = userRepository.findById(request.user_db_Id())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = imageStorage.uploadPostImage(imageFile);
        }

        Post post = new Post();
        post.setContent(request.content());
        post.setImageUrl(imageUrl);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        return dtoMapper.toPostResponse(savedPost, author, List.of());
    }

    public PostResponse getPostById(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post);
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> dtoMapper.toCommentResponse(comment, comment.getAuthor(), currentUser))
                .collect(Collectors.toList());

        return dtoMapper.toPostResponse(post, currentUser, commentResponses);
    }

    public List<PostResponse> getAllPosts(Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Post> posts = postRepository.findAllOrderByCreatedAtDesc();

        return posts.stream()
                .map(post -> dtoMapper.toPostResponse(post, currentUser, List.of()))
                .collect(Collectors.toList());
    }

    public List<PostResponse> getUserPosts(Long userId, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("Current user not found"));

        List<Post> posts = postRepository.findByAuthorOrderByCreatedAtDesc(user);

        return posts.stream()
                .map(post -> dtoMapper.toPostResponse(post, currentUser, List.of()))
                .collect(Collectors.toList());
    }

    public List<PostResponse> getFeed(Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Get IDs of users that current user follows + current user's own ID
        List<Long> followingIds = currentUser.getFollowing().stream()
                .map(connection -> connection.getFollowing().getUser_db_Id())
                .collect(Collectors.toList());
        followingIds.add(currentUserId);

        List<Post> posts = postRepository.findFeedPosts(followingIds);

        return posts.stream()
                .map(post -> dtoMapper.toPostResponse(post, currentUser, List.of()))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse updatePost(UpdatePostRequest request, Long currentUserId) {
        if (request.content() == null || request.content().isBlank()) {
            throw new PostContentEmptyException("Post content cannot be empty");
        }

        Post post = postRepository.findById(request.post_db_id())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!post.getAuthor().getUser_db_Id().equals(currentUserId)) {
            throw new PostUpdateNotAllowedException("You are not allowed to update this post");
        }

        post.setContent(request.content());
        post.setImageUrl(request.imageUrl());
        post.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);

        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post);
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> dtoMapper.toCommentResponse(comment, comment.getAuthor(), currentUser))
                .collect(Collectors.toList());

        return dtoMapper.toPostResponse(updatedPost, currentUser, commentResponses);
    }

    @Transactional
    public void deletePost(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        if (!post.getAuthor().getUser_db_Id().equals(currentUserId)) {
            throw new PostDeleteNotAllowedException("You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }

    // ========== COMMENT OPERATIONS ==========

    @Transactional
    public CommentResponse createComment(CreateCommentRequest request, Long currentUserId) {
        if (request.content() == null || request.content().isBlank()) {
            throw new CommentContentEmptyException("Comment content cannot be empty");
        }

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        User author = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = dtoMapper.toCommentEntity(request);
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return dtoMapper.toCommentResponse(savedComment, author, author);
    }

    public List<CommentResponse> getPostComments(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post);

        return comments.stream()
                .map(comment -> dtoMapper.toCommentResponse(comment, comment.getAuthor(), currentUser))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(UpdateCommentRequest request, Long currentUserId) {
        if (request.content() == null || request.content().isBlank()) {
            throw new CommentContentEmptyException("Comment content cannot be empty");
        }

        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!comment.getAuthor().getUser_db_Id().equals(currentUserId)) {
            throw new CommentDeleteNotAllowedException("You are not allowed to update this comment");
        }

        comment.setContent(request.content());
        Comment updatedComment = commentRepository.save(comment);

        return dtoMapper.toCommentResponse(updatedComment, currentUser, currentUser);
    }

    @Transactional
    public void deleteComment(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        if (!comment.getAuthor().getUser_db_Id().equals(currentUserId)) {
            throw new CommentDeleteNotAllowedException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }

    // ========== LIKE OPERATIONS ==========

    @Transactional
    public LikeResponse likePost(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new LikeAlreadyExistsException("You have already liked this post");
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        like.setCreatedAt(LocalDateTime.now());

        Like savedLike = likeRepository.save(like);

        return dtoMapper.toLikeResponse(savedLike);
    }

    @Transactional
    public void unlikePost(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new LikeNotFoundException("Like not found"));

        likeRepository.delete(like);
    }

    public List<LikeResponse> getPostLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        List<Like> likes = likeRepository.findByPostOrderByCreatedAtDesc(post);

        return likes.stream()
                .map(dtoMapper::toLikeResponse)
                .collect(Collectors.toList());
    }
}

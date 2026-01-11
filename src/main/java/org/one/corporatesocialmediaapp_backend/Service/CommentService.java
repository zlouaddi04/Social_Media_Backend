package org.one.corporatesocialmediaapp_backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.DTO.CommentResponse;
import org.one.corporatesocialmediaapp_backend.DTO.CreateCommentRequest;
import org.one.corporatesocialmediaapp_backend.DTO.UpdateCommentRequest;
import org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions.CommentContentEmptyException;
import org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions.CommentDeleteNotAllowedException;
import org.one.corporatesocialmediaapp_backend.Exceptions.CommentExceptions.CommentNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.PostExceptions.PostNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserNotFoundException;
import org.one.corporatesocialmediaapp_backend.Mapper.DTOMapper;
import org.one.corporatesocialmediaapp_backend.Models.Comment;
import org.one.corporatesocialmediaapp_backend.Models.Post;
import org.one.corporatesocialmediaapp_backend.Models.User;
import org.one.corporatesocialmediaapp_backend.Repositories.CommentRepository;
import org.one.corporatesocialmediaapp_backend.Repositories.PostRepository;
import org.one.corporatesocialmediaapp_backend.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final DTOMapper dtoMapper;

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

    public CommentResponse getCommentById(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return dtoMapper.toCommentResponse(comment, comment.getAuthor(), currentUser);
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
}


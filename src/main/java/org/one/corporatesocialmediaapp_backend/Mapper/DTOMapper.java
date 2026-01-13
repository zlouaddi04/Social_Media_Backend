package org.one.corporatesocialmediaapp_backend.Mapper;

import org.one.corporatesocialmediaapp_backend.DTO.*;
import org.one.corporatesocialmediaapp_backend.Models.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DTOMapper {

    // ==========USER==========

    public User toUserEntity(UserRegistrationRequest Request) {
        User user = new User();
        user.setUsername(Request.username());
        user.setPassword(Request.password());
        user.setFullName(Request.fullName());
        user.setEmail(Request.email());
        user.setDepartment(Request.department());
        user.setPosition(Request.position());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    public UserSummaryDTO toUserSummaryDTO(User user) {
        return new UserSummaryDTO(
                user.getUser_db_Id(),
                user.getUsername(),
                user.getFullName()
        );
    }

    public User UserUpdateRequest(UserUpdateRequest Request,User user) {
        user.setUser_db_Id((Request.userId()));
        user.setFullName(Request.fullName());
        user.setPosition(Request.position());
        user.setDepartment(Request.department());
        return user;
    }

    public UserProfileResponse toUserProfileResponse(User user,User currentUser) {
        UserProfileResponse userProfileResponse = new UserProfileResponse(
                user.getUser_db_Id(),
                user.getUsername(),
                user.getFullName(),
                user.getProfilePicture(),
                user.getPosition(),
                user.getDepartment(),
                user.getCreatedAt(),
                user.getFollowers().size(),
                user.getFollowing().size(),
                currentUser.getFollowing()
                        .stream()
                        .anyMatch(Conn->Conn.getFollowing().getUser_db_Id().equals(user.getUser_db_Id()))

        );
        return userProfileResponse;
    }


    // ==========POST==========


    public Post toPostEntity(CreatePostRequest request) {
        Post post = new Post();
        post.setContent(request.content());
        post.setImageUrl(request.imageUrl());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return post;
    }

    public PostSummaryDTO toPostSummaryDTO(Post post,User post_author) {
        return new PostSummaryDTO(
                post.getPost_db_id(),
                post.getContent(),
                post.getImageUrl(),
                toUserSummaryDTO(post_author)
        );
    }

    public PostResponse toPostResponse(Post post, User Curent_user, List<CommentResponse> comments) {
        return new PostResponse(
                post.getPost_db_id(),
                post.getContent(),
                post.getImageUrl(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                toUserSummaryDTO(post.getAuthor()),
                post.getLikes().size(),
                post.getComments().size(),
                post.getLikes()
                        .stream()
                        .anyMatch(like-> like.getUser().getUser_db_Id().equals(Curent_user.getUser_db_Id())),
                comments

        );
    }

    public Post toUpdatedpost(UpdatePostRequest Request) {
        Post post = new Post();
        post.setPost_db_id(Request.post_db_id());
        post.setContent(Request.content());
        post.setImageUrl(Request.imageUrl());
        return post;
    }

    // ==========COMMENTS==========

    public Comment toCommentEntity(CreateCommentRequest request) {
        Comment comment = new Comment();
        comment.setContent(request.content());
        comment.setCreatedAt(LocalDateTime.now());
        return comment;

    }

    public CommentResponse toCommentResponse(Comment comment,User author,User currentUser) {
        return new CommentResponse(
                comment.getComment_db_id(),
                comment.getContent(),
                comment.getCreatedAt(),
                toUserSummaryDTO(author),
                author.equals(currentUser)

        );
    }

    public Comment UpdatedComment(UpdateCommentRequest request) {
        Comment comment = new Comment();
        comment.setComment_db_id(request.commentId());
        comment.setContent(request.content());
        return comment;
    }

    // ==========LIKES==========

    public LikeResponse toLikeResponse(Like like) {
        return new LikeResponse(
                toUserSummaryDTO(like.getUser()),
                like.getCreatedAt()
        );
    }

    // ==========CONNECTION==========

    public ConnectionResponse toConnectionResponse(Connection connection) {
        return new ConnectionResponse(
                connection.getId(),
                toUserSummaryDTO(connection.getFollower()),
                toUserSummaryDTO(connection.getFollowing()),
                connection.getCreatedAt()

        );
    }




}

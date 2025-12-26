package org.one.corporatesocialmediaapp_backend.Mapper;

import org.one.corporatesocialmediaapp_backend.DTO.*;
import org.one.corporatesocialmediaapp_backend.Models.Comment;
import org.one.corporatesocialmediaapp_backend.Models.Post;
import org.one.corporatesocialmediaapp_backend.Models.User;

import java.time.LocalDateTime;

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
        UserSummaryDTO userSummaryDTO = new UserSummaryDTO(
                user.getUser_db_Id(),
                user.getUsername(),
                user.getFullName()
        );
        return userSummaryDTO;
    }

    public User UserUpdateRequest(UserUpdateRequest Request) {
        User user = new User();
        user.setUser_db_Id((Request.userId()));
        user.setFullName(Request.fullName());
        user.setProfilePicture(Request.profilePicture());
        user.setPosition(Request.position());
        user.setDepartment(Request.department());
        return user;
    }

    public UserProfileResponse toUserProfileResponse(User user) {
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
                null

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

    public PostSummaryDTO toPostSummaryDTO(Post post) {
        return new PostSummaryDTO(
                post.getPost_db_id(),
                post.getContent(),
                post.getImageUrl(),
                null
        );
    }

    public PostResponse toPostResponse(Post post) {
        return new PostResponse(
                post.getPost_db_id(),
                post.getContent(),
                post.getImageUrl(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getAuthor().getUser_db_Id(),
                post.getLikes().size(),
                post.getComments().size(),
                null,
                null
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


}

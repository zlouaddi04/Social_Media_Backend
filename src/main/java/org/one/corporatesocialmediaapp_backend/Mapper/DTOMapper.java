package org.one.corporatesocialmediaapp_backend.Mapper;

import org.one.corporatesocialmediaapp_backend.DTO.*;
import org.one.corporatesocialmediaapp_backend.Models.Post;
import org.one.corporatesocialmediaapp_backend.Models.User;

import java.time.LocalDateTime;

public class DTOMapper {

    // ==========USER==========

    public User toUserEntity(UserRegistrationRequest Request){
        User user=new User();
        user.setUsername(Request.username());
        user.setPassword(Request.password());
        user.setFullName(Request.fullName());
        user.setEmail(Request.email());
        user.setDepartment(Request.department());
        user.setPosition(Request.position());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    public UserSummaryDTO toUserSummaryDTO(User user){
        UserSummaryDTO userSummaryDTO=new UserSummaryDTO(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getProfilePicture(),
                user.getPosition(),
                user.getDepartment(),
                null,
                null
        );
        return userSummaryDTO;
    }
    public User UserUpdateRequest(UserUpdateRequest Request){
        User user =new User();
        user.setUserId((Request.userId()));
        user.setFullName(Request.fullName());
        user.setProfilePicture(Request.profilePicture());
        user.setPosition(Request.position());
        user.setDepartment(Request.department());
        return user;
    }

    public UserProfileResponse toUserProfileResponse(User user){
        UserProfileResponse userProfileResponse=new UserProfileResponse(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getProfilePicture(),
                user.getPosition(),
                user.getDepartment(),
                user.getCreatedAt(),
                null,
                null,
                null

        );
        return userProfileResponse;
    }





    // ==========POST==========



    public Post toPostEntity(CreatePostRequest request){
        Post post=new Post();
        post.setContent(request.content());
        post.setImageUrl(request.imageUrl());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return post;
    }
}

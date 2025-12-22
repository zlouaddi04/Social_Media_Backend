package org.one.corporatesocialmediaapp_backend.Mapper;

import org.one.corporatesocialmediaapp_backend.DTO.UserRegistrationRequest;
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




}

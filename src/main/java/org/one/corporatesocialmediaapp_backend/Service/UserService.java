package org.one.corporatesocialmediaapp_backend.Service;

import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.DTO.UserRegistrationRequest;
import org.one.corporatesocialmediaapp_backend.DTO.UserSummaryDTO;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserEmailAlreadyExists;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserUsernameAlreadyExists;
import org.one.corporatesocialmediaapp_backend.Mapper.DTOMapper;
import org.one.corporatesocialmediaapp_backend.Models.User;
import org.one.corporatesocialmediaapp_backend.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    final UserRepository userRepository;
    final DTOMapper dtoMapper;
    final PasswordEncoder passwordEncoder;

    @Transactional
    public UserSummaryDTO regiterUser(UserRegistrationRequest Request){
        if (userRepository.existsByEmail(Request.email()))
            throw new UserEmailAlreadyExists("Email already used");
        if (userRepository.existsByUsername(Request.username()))
            throw new UserUsernameAlreadyExists("Username already used");

        //MAPPING_TO_USER
        User newUser=dtoMapper.toUserEntity(Request);

        //HASHING_PASSWORD
        String hashed_password= passwordEncoder.encode(Request.password());
        newUser.setPassword(hashed_password);

        //PERSIST_USER
        User savedUser= userRepository.save(newUser);
        return dtoMapper.toUserSummaryDTO(savedUser);
    }

    public List<UserSummaryDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user-> dtoMapper.toUserSummaryDTO(user))
                .toList();

    }


}

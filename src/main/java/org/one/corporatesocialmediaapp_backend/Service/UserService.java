package org.one.corporatesocialmediaapp_backend.Service;

import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.DTO.*;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserEmailAlreadyExists;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserUsernameAlreadyExists;
import org.one.corporatesocialmediaapp_backend.Mapper.DTOMapper;
import org.one.corporatesocialmediaapp_backend.Models.User;
import org.one.corporatesocialmediaapp_backend.Repositories.UserRepository;
import org.one.corporatesocialmediaapp_backend.Service.StorageService.ImageStorage;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    final UserRepository userRepository;
    final DTOMapper dtoMapper;
    final PasswordEncoder passwordEncoder;
    final ImageStorage  imageStorage;





    public boolean AuthenticateUser(UserLoginRequest userLoginRequest) {
        Optional<User> optionnalUser=userRepository.findByUsername(userLoginRequest.username());
        if (optionnalUser.isEmpty()) {
            throw new UserNotFoundException("User with username " + userLoginRequest.username() + " not found");
        }
        User user=optionnalUser.get();
        return passwordEncoder.matches(userLoginRequest.password(), user.getPassword());

    }


    public List<UserSummaryDTO> getAllUsers() {
        return userRepository.findAllSummaries();

    }

    public UserProfileResponse getUserProfile(Long userID,User currentUser){
        Optional<User> Optional_User=userRepository.findById(userID);
        if (Optional_User.isEmpty())
            throw new UserNotFoundException("User not found");
        User user=Optional_User.get();
        return dtoMapper.toUserProfileResponse(user,currentUser);

    }

    public UserSummaryDTO getUserSummary(Long userID){
        Optional<User> Optional_User=userRepository.findById(userID);
        if (Optional_User.isEmpty())
            throw new UserNotFoundException("User not found");
        User user=Optional_User.get();
        return dtoMapper.toUserSummaryDTO(user);
    }

    public List<UserSummaryDTO> getUserSearchResults(String username,Pageable pageable){
        return userRepository.findAllSummariesMatching(username,pageable);
    }




    public List<FollowerListResponse> getFollowers(Long userID){
        if (!userRepository.existsById(userID)){
            throw new UserNotFoundException("User not found");
        }
        return userRepository.findMyFollowers(userID);
    }

    public List<FollowingListResponse> getFollowings(Long userID){
        if (!userRepository.existsById(userID)){
            throw new UserNotFoundException("User not found");
        }
        return userRepository.findMyFollowings(userID);
    }




    @Transactional
    public UserSummaryDTO regiterUser(MultipartFile profilePicture, UserRegistrationRequest Request){
        if (userRepository.existsByEmail(Request.email()))
            throw new UserEmailAlreadyExists("Email already used");
        if (userRepository.existsByUsername(Request.username()))
            throw new UserUsernameAlreadyExists("Username already used");

        //MAPPING_TO_USER
        User newUser=dtoMapper.toUserEntity(Request);

        //STORING_PROFILE_PICTURE (optional)
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String imageURL = imageStorage.uploadProfilePicture(profilePicture);
            newUser.setProfilePicture(imageURL);
        }

        //HASHING_PASSWORD
        String hashed_password= passwordEncoder.encode(Request.password());
        newUser.setPassword(hashed_password);

        //PERSIST_USER
        User savedUser= userRepository.save(newUser);
        return dtoMapper.toUserSummaryDTO(savedUser);
    }



    @Transactional
    public UserSummaryDTO updateUser(UserUpdateRequest request){
        Optional<User> Optional_User=userRepository.findById(request.userId());
        if (Optional_User.isEmpty())
            throw new UserNotFoundException("User not found");
        User user=Optional_User.get();

        //MAP_UPDATE_REQUEST_TO_ENTITY
        User UpdatedUser=dtoMapper.UserUpdateRequest(request,user);

        //PERSIST_ENTITY
        userRepository.save(UpdatedUser);
        return dtoMapper.toUserSummaryDTO(UpdatedUser);

    }

    @Transactional
    public void updatePassword(Long userID,String newPassword){
        Optional<User> Optional_User=userRepository.findById(userID);
        if (Optional_User.isEmpty())
            throw new UserNotFoundException("User not found");
        User user=Optional_User.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }








}

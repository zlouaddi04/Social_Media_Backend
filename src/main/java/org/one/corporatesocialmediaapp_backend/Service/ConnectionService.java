package org.one.corporatesocialmediaapp_backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.DTO.ConnectionResponse;
import org.one.corporatesocialmediaapp_backend.DTO.FollowerListResponse;
import org.one.corporatesocialmediaapp_backend.DTO.FollowingListResponse;
import org.one.corporatesocialmediaapp_backend.Exceptions.ConnectionExceptions.ConnectionAlreadyExistsException;
import org.one.corporatesocialmediaapp_backend.Exceptions.ConnectionExceptions.ConnectionNotFoundException;
import org.one.corporatesocialmediaapp_backend.Exceptions.ConnectionExceptions.FollowSelfNotAllowedException;
import org.one.corporatesocialmediaapp_backend.Exceptions.UserExceptions.UserNotFoundException;
import org.one.corporatesocialmediaapp_backend.Mapper.DTOMapper;
import org.one.corporatesocialmediaapp_backend.Models.Connection;
import org.one.corporatesocialmediaapp_backend.Models.User;
import org.one.corporatesocialmediaapp_backend.Repositories.ConnectionRepository;
import org.one.corporatesocialmediaapp_backend.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;
    private final DTOMapper dtoMapper;

    @Transactional
    public ConnectionResponse followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new FollowSelfNotAllowedException("You cannot follow yourself");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException("Follower user not found"));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("Following user not found"));

        if (connectionRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new ConnectionAlreadyExistsException("You are already following this user");
        }

        Connection connection = new Connection();
        connection.setFollower(follower);
        connection.setFollowing(following);
        connection.setCreatedAt(LocalDateTime.now());

        Connection savedConnection = connectionRepository.save(connection);

        return dtoMapper.toConnectionResponse(savedConnection);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException("Follower user not found"));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("Following user not found"));

        Connection connection = connectionRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new ConnectionNotFoundException("Connection not found"));

        connectionRepository.delete(connection);
    }

    public List<FollowerListResponse> getFollowers(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        return userRepository.findMyFollowers(userId);
    }

    public List<FollowingListResponse> getFollowing(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        return userRepository.findMyFollowings(userId);
    }
}


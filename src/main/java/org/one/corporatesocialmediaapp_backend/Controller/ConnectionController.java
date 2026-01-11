package org.one.corporatesocialmediaapp_backend.Controller;

import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.DTO.ConnectionResponse;
import org.one.corporatesocialmediaapp_backend.DTO.FollowerListResponse;
import org.one.corporatesocialmediaapp_backend.DTO.FollowingListResponse;
import org.one.corporatesocialmediaapp_backend.Service.ConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
@AllArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @PostMapping("/follow")
    public ResponseEntity<ConnectionResponse> followUser(
            @RequestParam("followerId") Long followerId,
            @RequestParam("followingId") Long followingId) {
        ConnectionResponse response = connectionService.followUser(followerId, followingId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<Void> unfollowUser(
            @RequestParam("followerId") Long followerId,
            @RequestParam("followingId") Long followingId) {
        connectionService.unfollowUser(followerId, followingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowerListResponse>> getFollowers(@PathVariable Long userId) {
        List<FollowerListResponse> response = connectionService.getFollowers(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowingListResponse>> getFollowing(@PathVariable Long userId) {
        List<FollowingListResponse> response = connectionService.getFollowing(userId);
        return ResponseEntity.ok(response);
    }
}


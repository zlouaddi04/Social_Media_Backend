package org.one.corporatesocialmediaapp_backend.Controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.DTO.*;
import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;
import org.one.corporatesocialmediaapp_backend.Service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserSummaryDTO> registerUser(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("fullName") String fullName,
            @RequestParam("position") Position position,
            @RequestParam("department") Department department,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {

        UserRegistrationRequest request = new UserRegistrationRequest(
                username, email, password, fullName, position, department);
        UserSummaryDTO response = userService.regiterUser(profilePicture, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserSummaryDTO>> getAllUsers() {
        List<UserSummaryDTO> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserSummaryDTO> getUserSummary(@PathVariable Long userId) {
        UserSummaryDTO response = userService.getUserSummary(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(
            @PathVariable Long userId,
            @RequestParam("currentUserId") Long currentUserId) {
        // In real implementation, currentUser should be extracted from JWT/session
        // For now we're passing it as a parameter
        org.one.corporatesocialmediaapp_backend.Models.User currentUser =
                new org.one.corporatesocialmediaapp_backend.Models.User();
        currentUser.setUser_db_Id(currentUserId);

        UserProfileResponse response = userService.getUserProfile(userId, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSummaryDTO>> searchUsers(
            @RequestParam("query") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<UserSummaryDTO> response = userService.getUserSearchResults(query, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSummaryDTO> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        UserSummaryDTO response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long userId,
            @RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(userId, request.newPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowerListResponse>> getFollowers(@PathVariable Long userId) {
        List<FollowerListResponse> response = userService.getFollowers(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowingListResponse>> getFollowing(@PathVariable Long userId) {
        List<FollowingListResponse> response = userService.getFollowings(userId);
        return ResponseEntity.ok(response);
    }

    // Simple DTO for password update
    public record PasswordUpdateRequest(String newPassword) {}
}

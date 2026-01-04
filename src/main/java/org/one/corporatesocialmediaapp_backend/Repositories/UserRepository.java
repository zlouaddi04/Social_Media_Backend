package org.one.corporatesocialmediaapp_backend.Repositories;

import org.one.corporatesocialmediaapp_backend.DTO.FollowerListResponse;
import org.one.corporatesocialmediaapp_backend.DTO.FollowingListResponse;
import org.one.corporatesocialmediaapp_backend.DTO.UserSummaryDTO;
import org.one.corporatesocialmediaapp_backend.Models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query("""
    SELECT new org.one.corporatesocialmediaapp_backend.DTO.UserSummaryDTO(
        u.User_db_Id,
        u.username,
        u.fullName
    )
    FROM User u
    """)
    List<UserSummaryDTO> findAllSummaries();


    @Query("""
    SELECT new org.one.corporatesocialmediaapp_backend.DTO.UserSummaryDTO(
        u.User_db_Id,
        u.username,
        u.fullName
    )
    FROM User u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT(:PREFIX,'%'))
    ORDER BY u.username
    
    """)
    List<UserSummaryDTO> findAllSummariesMatching(@Param("PREFIX") String username, Pageable pageable);


    @Query("""
    SELECT new org.one.corporatesocialmediaapp_backend.DTO.FollowerListResponse(
        new org.one.corporatesocialmediaapp_backend.DTO.UserSummaryDTO(
            follower.User_db_Id,
            follower.username,
            follower.fullName
        ),
        conn.createdAt,
        CASE
            WHEN EXISTS (
                SELECT c2
                FROM Connection c2
                WHERE c2.follower.User_db_Id = :currentUserId
                  AND c2.following.User_db_Id = follower.User_db_Id
            )
            THEN true
            ELSE false
        END
    )
    FROM Connection conn
    JOIN conn.follower follower
    WHERE conn.following.User_db_Id = :currentUserId
""")
    List<FollowerListResponse> findMyFollowers(
            @Param("currentUserId") Long currentUserId
    );


    @Query("""
    SELECT new org.one.corporatesocialmediaapp_backend.DTO.FollowerListResponse(
        new org.one.corporatesocialmediaapp_backend.DTO.UserSummaryDTO(
            following.User_db_Id,
            following.username,
            following.fullName
        ),
        conn.createdAt,
        CASE
            WHEN EXISTS (
                SELECT c2
                FROM Connection c2
                WHERE c2.follower.User_db_Id = :currentUserId
                  AND c2.following.User_db_Id = following.User_db_Id
            )
            THEN true
            ELSE false
        END
    )
    FROM Connection conn
    JOIN conn.following following
    WHERE conn.follower.User_db_Id = :currentUserId
""")
    List<FollowingListResponse> findMyFollowings(
            @Param("currentUserId") Long currentUserId
    );






}

package org.one.corporatesocialmediaapp_backend.Repositories;

import org.one.corporatesocialmediaapp_backend.Models.Connection;
import org.one.corporatesocialmediaapp_backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    Optional<Connection> findByFollowerAndFollowing(User follower, User following);

    void deleteByFollowerAndFollowing(User follower, User following);
}


package org.one.corporatesocialmediaapp_backend.Repositories;

import org.one.corporatesocialmediaapp_backend.Models.Like;
import org.one.corporatesocialmediaapp_backend.Models.Post;
import org.one.corporatesocialmediaapp_backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(User user, Post post);

    boolean existsByUserAndPost(User user, Post post);

    List<Like> findByPostOrderByCreatedAtDesc(Post post);

    void deleteByPost(Post post);
}


package org.one.corporatesocialmediaapp_backend.Repositories;

import org.one.corporatesocialmediaapp_backend.Models.Comment;
import org.one.corporatesocialmediaapp_backend.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostOrderByCreatedAtDesc(Post post);

    void deleteByPost(Post post);
}


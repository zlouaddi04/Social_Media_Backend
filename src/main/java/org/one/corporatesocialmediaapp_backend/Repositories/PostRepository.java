package org.one.corporatesocialmediaapp_backend.Repositories;

import org.one.corporatesocialmediaapp_backend.Models.Post;
import org.one.corporatesocialmediaapp_backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    List<Post> findByAuthorOrderByCreatedAtDesc(User author);

    @Query("""
        SELECT p FROM Post p
        WHERE p.author.User_db_Id IN :userIds
        ORDER BY p.createdAt DESC
    """)
    List<Post> findFeedPosts(@Param("userIds") List<Long> userIds);

    @Query("""
        SELECT p FROM Post p
        ORDER BY p.createdAt DESC
    """)
    List<Post> findAllOrderByCreatedAtDesc();
}

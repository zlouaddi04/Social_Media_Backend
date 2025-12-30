package org.one.corporatesocialmediaapp_backend.Repositories;

import org.one.corporatesocialmediaapp_backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

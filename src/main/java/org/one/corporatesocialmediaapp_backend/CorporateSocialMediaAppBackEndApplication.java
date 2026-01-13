package org.one.corporatesocialmediaapp_backend;

import lombok.extern.slf4j.Slf4j;
import org.one.corporatesocialmediaapp_backend.Enums.Department;
import org.one.corporatesocialmediaapp_backend.Enums.Position;
import org.one.corporatesocialmediaapp_backend.Models.Connection;
import org.one.corporatesocialmediaapp_backend.Models.Post;
import org.one.corporatesocialmediaapp_backend.Models.User;
import org.one.corporatesocialmediaapp_backend.Repositories.ConnectionRepository;
import org.one.corporatesocialmediaapp_backend.Repositories.PostRepository;
import org.one.corporatesocialmediaapp_backend.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class CorporateSocialMediaAppBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorporateSocialMediaAppBackEndApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            PostRepository postRepository,
            ConnectionRepository connectionRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Check if users already exist
            if (userRepository.count() > 0) {
                log.info("Database already seeded, skipping initialization");
                return;
            }

            log.info("Starting database initialization...");

            // Create 3 users
            User user1 = new User();
            user1.setUsername("zaki2004");
            user1.setEmail("zakaria@company.com");
            user1.setPassword(passwordEncoder.encode("password123"));
            user1.setFullName("Louaddi zakaria");
            user1.setPosition(Position.SENIOR);
            user1.setDepartment(Department.ENGINEERING);
            user1.setCreatedAt(LocalDateTime.now());
            user1 = userRepository.save(user1);
            log.info("Created user: {}", user1.getUsername());

            User user2 = new User();
            user2.setUsername("hamza2026");
            user2.setEmail("hamza@company.com");
            user2.setPassword(passwordEncoder.encode("password456"));
            user2.setFullName("hamza zaki");
            user2.setPosition(Position.MANAGER);
            user2.setDepartment(Department.MARKETING);
            user2.setCreatedAt(LocalDateTime.now());
            user2 = userRepository.save(user2);
            log.info("Created user: {}", user2.getUsername());

            User user3 = new User();
            user3.setUsername("flan");
            user3.setEmail("flan@company.com");
            user3.setPassword(passwordEncoder.encode("password789"));
            user3.setFullName("flan flan");
            user3.setPosition(Position.JUNIOR);
            user3.setDepartment(Department.SALES);
            user3.setCreatedAt(LocalDateTime.now());
            user3 = userRepository.save(user3);
            log.info("Created user: {}", user3.getUsername());

            // Create connections (all follow each other)
            // User1 follows User2 and User3
            Connection conn1 = new Connection();
            conn1.setFollower(user1);
            conn1.setFollowing(user2);
            conn1.setCreatedAt(LocalDateTime.now());
            connectionRepository.save(conn1);
            log.info("{} follows {}", user1.getUsername(), user2.getUsername());

            Connection conn2 = new Connection();
            conn2.setFollower(user1);
            conn2.setFollowing(user3);
            conn2.setCreatedAt(LocalDateTime.now());
            connectionRepository.save(conn2);
            log.info("{} follows {}", user1.getUsername(), user3.getUsername());

            // User2 follows User1 and User3
            Connection conn3 = new Connection();
            conn3.setFollower(user2);
            conn3.setFollowing(user1);
            conn3.setCreatedAt(LocalDateTime.now());
            connectionRepository.save(conn3);
            log.info("{} follows {}", user2.getUsername(), user1.getUsername());

            Connection conn4 = new Connection();
            conn4.setFollower(user2);
            conn4.setFollowing(user3);
            conn4.setCreatedAt(LocalDateTime.now());
            connectionRepository.save(conn4);
            log.info("{} follows {}", user2.getUsername(), user3.getUsername());

            // User3 follows User1 and User2
            Connection conn5 = new Connection();
            conn5.setFollower(user3);
            conn5.setFollowing(user1);
            conn5.setCreatedAt(LocalDateTime.now());
            connectionRepository.save(conn5);
            log.info("{} follows {}", user3.getUsername(), user1.getUsername());

            Connection conn6 = new Connection();
            conn6.setFollower(user3);
            conn6.setFollowing(user2);
            conn6.setCreatedAt(LocalDateTime.now());
            connectionRepository.save(conn6);
            log.info("{} follows {}", user3.getUsername(), user2.getUsername());

            // Create 2 text-only posts for each user
            // User1 posts
            Post post1 = new Post();
            post1.setContent("Excited to join the team! Looking forward to working with everyone.");
            post1.setAuthor(user1);
            post1.setCreatedAt(LocalDateTime.now().minusDays(2));
            post1.setUpdatedAt(LocalDateTime.now().minusDays(2));
            postRepository.save(post1);
            log.info("Created post by {}: {}", user1.getUsername(), post1.getContent().substring(0, 30) + "...");

            Post post2 = new Post();
            post2.setContent("Just finished a great project on microservices architecture. The journey was challenging but rewarding!");
            post2.setAuthor(user1);
            post2.setCreatedAt(LocalDateTime.now().minusDays(1));
            post2.setUpdatedAt(LocalDateTime.now().minusDays(1));
            postRepository.save(post2);
            log.info("Created post by {}: {}", user1.getUsername(), post2.getContent().substring(0, 30) + "...");

            // User2 posts
            Post post3 = new Post();
            post3.setContent("Our new marketing campaign is launching next week! Can't wait to share the results with everyone.");
            post3.setAuthor(user2);
            post3.setCreatedAt(LocalDateTime.now().minusDays(2));
            post3.setUpdatedAt(LocalDateTime.now().minusDays(2));
            postRepository.save(post3);
            log.info("Created post by {}: {}", user2.getUsername(), post3.getContent().substring(0, 30) + "...");

            Post post4 = new Post();
            post4.setContent("Team meeting was productive today. Great ideas from everyone! Let's keep up the momentum.");
            post4.setAuthor(user2);
            post4.setCreatedAt(LocalDateTime.now().minusHours(12));
            post4.setUpdatedAt(LocalDateTime.now().minusHours(12));
            postRepository.save(post4);
            log.info("Created post by {}: {}", user2.getUsername(), post4.getContent().substring(0, 30) + "...");

            // User3 posts
            Post post5 = new Post();
            post5.setContent("Closed my first deal today! Thanks to everyone who helped me learn the ropes.");
            post5.setAuthor(user3);
            post5.setCreatedAt(LocalDateTime.now().minusDays(1));
            post5.setUpdatedAt(LocalDateTime.now().minusDays(1));
            postRepository.save(post5);
            log.info("Created post by {}: {}", user3.getUsername(), post5.getContent().substring(0, 30) + "...");

            Post post6 = new Post();
            post6.setContent("Looking forward to the company retreat next month. It's going to be a great opportunity to connect with colleagues!");
            post6.setAuthor(user3);
            post6.setCreatedAt(LocalDateTime.now().minusHours(6));
            post6.setUpdatedAt(LocalDateTime.now().minusHours(6));
            postRepository.save(post6);
            log.info("Created post by {}: {}", user3.getUsername(), post6.getContent().substring(0, 30) + "...");

            log.info("Database initialization completed successfully!");
            log.info("Created {} users, {} connections, {} posts",
                    userRepository.count(),
                    connectionRepository.count(),
                    postRepository.count());
        };
    }
}

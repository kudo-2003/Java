package run_main.spring_maven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import run_main.spring_maven.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Add custom query methods if needed
}
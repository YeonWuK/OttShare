package project.ottshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.ottshare.entity.User;
import project.ottshare.repository.custom.UserRepositoryCustom;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> , UserRepositoryCustom {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
    Optional<User> findByNameAndUsernameAndEmail (String name, String username, String email);
}

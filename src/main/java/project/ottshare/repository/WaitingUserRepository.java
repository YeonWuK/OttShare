package project.ottshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.ottshare.entity.WaitingUser;
import project.ottshare.enums.OttType;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingUserRepository extends JpaRepository<WaitingUser, Long> {
    Optional<WaitingUser> findByUserId(Long userId);
    Optional<WaitingUser> findByIsLeaderTrueAndOttType(OttType ottType);
    List<WaitingUser> findByIsLeaderFalseAndOttType(OttType ottType);
}

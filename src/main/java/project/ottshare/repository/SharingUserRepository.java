package project.ottshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.ottshare.entity.SharingUser;
import project.ottshare.repository.custom.SharingUserRepositoryCustom;

import java.util.Optional;

@Repository
public interface SharingUserRepository extends JpaRepository<SharingUser, Long>, SharingUserRepositoryCustom {
    Optional<SharingUser> findByUserId(Long userId);
    Optional<SharingUser> findUserByOttShareRoomIdAndUserId(Long roomId, Long userId);
    boolean existsByUserId(Long userId);
}

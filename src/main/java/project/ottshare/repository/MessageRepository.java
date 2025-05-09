package project.ottshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.ottshare.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}

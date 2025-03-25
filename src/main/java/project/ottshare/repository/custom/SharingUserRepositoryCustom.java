package project.ottshare.repository.custom;

import java.util.List;

public interface SharingUserRepositoryCustom {
    List<Long> findAllUserIdsByOttShareRoomId(Long roomId);
}

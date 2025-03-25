package project.ottshare.repository.custom;

import java.util.List;

public interface UserRepositoryCustom {
    void UpdateIsShareRoom(List<Long> userIds);
    void UpdateUnShareRoom(List<Long> userIds);
}

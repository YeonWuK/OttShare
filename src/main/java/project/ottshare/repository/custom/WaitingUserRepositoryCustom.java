package project.ottshare.repository.custom;

import project.ottshare.entity.WaitingUser;
import project.ottshare.enums.OttType;

import java.util.List;

public interface WaitingUserRepositoryCustom {
    List<WaitingUser> findNonLeaderByOtt(OttType ottType, int limit);
}

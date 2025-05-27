package project.ottshare.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.ottshare.entity.WaitingUser;
import project.ottshare.enums.OttType;

import java.util.List;

import static project.ottshare.entity.QWaitingUser.waitingUser;

@Repository
@RequiredArgsConstructor
public class WaitingUserRepositoryCustomImpl implements WaitingUserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<WaitingUser> findNonLeaderByOtt(OttType ottType, int limit) {
        List<WaitingUser> result = jpaQueryFactory
                .select(waitingUser)
                .where(waitingUser.ottType.eq(ottType)
                        .and(waitingUser.isLeader.isFalse()))
                .orderBy(waitingUser.createTime.asc())
                .limit(limit)
                .fetch();

        return result;
    }
}

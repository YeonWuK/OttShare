package project.ottshare.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import project.ottshare.entity.QSharingUser;

import java.util.List;

@RequiredArgsConstructor
public class SharingUserRepositoryCustomImpl implements SharingUserRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findAllUserIdsByOttShareRoomId(Long roomId) {
        return queryFactory.select(QSharingUser.sharingUser.user.id)
                .from(QSharingUser.sharingUser)
                .where(QSharingUser.sharingUser.ottShareRoom.id.eq(roomId))
                .fetch();
    }
}

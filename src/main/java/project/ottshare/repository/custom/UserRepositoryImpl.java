package project.ottshare.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import project.ottshare.entity.QUser;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public void UpdateIsShareRoom(List<Long> userIds) {
        queryFactory.update(QUser.user)
                .set(QUser.user.isShareRoom, true)
                .where(QUser.user.id.in(userIds))
                .execute();

        entityManager.clear();
    }

    @Override
    public void UpdateUnShareRoom(List<Long> userIds) {
        queryFactory.update(QUser.user)
                .set(QUser.user.isShareRoom, false)
                .where(QUser.user.id.in(userIds))
                .execute();

        entityManager.clear();
    }
}

package project.ottshare.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWaitingUser is a Querydsl query type for WaitingUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWaitingUser extends EntityPathBase<WaitingUser> {

    private static final long serialVersionUID = -988517034L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWaitingUser waitingUser = new QWaitingUser("waitingUser");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isLeader = createBoolean("isLeader");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath ottId = createString("ottId");

    public final StringPath ottPassword = createString("ottPassword");

    public final EnumPath<project.ottshare.enums.OttType> ottType = createEnum("ottType", project.ottshare.enums.OttType.class);

    public final QUser user;

    public QWaitingUser(String variable) {
        this(WaitingUser.class, forVariable(variable), INITS);
    }

    public QWaitingUser(Path<? extends WaitingUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWaitingUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWaitingUser(PathMetadata metadata, PathInits inits) {
        this(WaitingUser.class, metadata, inits);
    }

    public QWaitingUser(Class<? extends WaitingUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}


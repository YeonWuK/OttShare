package project.ottshare.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSharingUser is a Querydsl query type for SharingUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSharingUser extends EntityPathBase<SharingUser> {

    private static final long serialVersionUID = -1382160283L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSharingUser sharingUser = new QSharingUser("sharingUser");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isChecked = createBoolean("isChecked");

    public final BooleanPath isLeader = createBoolean("isLeader");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QOttShareRoom ottShareRoom;

    public final QUser user;

    public QSharingUser(String variable) {
        this(SharingUser.class, forVariable(variable), INITS);
    }

    public QSharingUser(Path<? extends SharingUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSharingUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSharingUser(PathMetadata metadata, PathInits inits) {
        this(SharingUser.class, metadata, inits);
    }

    public QSharingUser(Class<? extends SharingUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ottShareRoom = inits.isInitialized("ottShareRoom") ? new QOttShareRoom(forProperty("ottShareRoom")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}


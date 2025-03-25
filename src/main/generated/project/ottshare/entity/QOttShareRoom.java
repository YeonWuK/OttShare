package project.ottshare.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOttShareRoom is a Querydsl query type for OttShareRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOttShareRoom extends EntityPathBase<OttShareRoom> {

    private static final long serialVersionUID = 188784909L;

    public static final QOttShareRoom ottShareRoom = new QOttShareRoom("ottShareRoom");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Message, QMessage> messages = this.<Message, QMessage>createList("messages", Message.class, QMessage.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath ottId = createString("ottId");

    public final StringPath ottPassword = createString("ottPassword");

    public final EnumPath<project.ottshare.enums.OttType> ottType = createEnum("ottType", project.ottshare.enums.OttType.class);

    public final ListPath<SharingUser, QSharingUser> sharingUsers = this.<SharingUser, QSharingUser>createList("sharingUsers", SharingUser.class, QSharingUser.class, PathInits.DIRECT2);

    public QOttShareRoom(String variable) {
        super(OttShareRoom.class, forVariable(variable));
    }

    public QOttShareRoom(Path<? extends OttShareRoom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOttShareRoom(PathMetadata metadata) {
        super(OttShareRoom.class, metadata);
    }

}


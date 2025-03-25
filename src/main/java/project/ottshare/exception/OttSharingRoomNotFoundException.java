package project.ottshare.exception;

public class OttSharingRoomNotFoundException extends RuntimeException {
    public OttSharingRoomNotFoundException(Long roomId, Long userId) {
        super("이미 삭제된 방입니다 :" + roomId + " 잘못된 접근 사용자 :" + userId);
    }

    public OttSharingRoomNotFoundException(String message) {
        super(message);
    }
}

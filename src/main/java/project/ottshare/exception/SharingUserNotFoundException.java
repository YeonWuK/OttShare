package project.ottshare.exception;

public class SharingUserNotFoundException extends RuntimeException {
    public SharingUserNotFoundException(Long userid) {
        super("해당 User 은 Sharing 중이지 않습니다. user_id : "+ userid);
    }
}


package project.ottshare.exception;

public class SharingUserNotCheckedException extends RuntimeException {
    public SharingUserNotCheckedException(Long userId) {
        super(userId + " 입금 확인 ❌");
    }
}

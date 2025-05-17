package project.ottshare.exception;

import project.ottshare.enums.OttType;

public class OttLeaderNotFoundException extends RuntimeException {
    public OttLeaderNotFoundException(OttType ott) {
        super(ott + "에 대한 리더를 찾을 수 없습니다.");
    }
}



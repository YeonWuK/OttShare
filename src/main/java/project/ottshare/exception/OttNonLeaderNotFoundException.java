package project.ottshare.exception;

import project.ottshare.enums.OttType;

public class OttNonLeaderNotFoundException extends RuntimeException {
    public OttNonLeaderNotFoundException (OttType ottType){
        super(ottType +"에 리더가 아닌 사용자를 인원이 부족합니다.");
    }
}

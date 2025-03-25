package project.ottshare.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OttType {

    NETFLIX("넷플릭스", 9000),
    TVING("티빙", 4250),
    WAVVE("웨이브", 3475);

    private final String value;
    private final int price;
}

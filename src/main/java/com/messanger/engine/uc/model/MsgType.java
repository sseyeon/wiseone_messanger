package com.messanger.engine.uc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MsgType {
    GENERAL("GNR", "일반메시지", "나눔고딕", "", "blue", 13),
    EMERGENCY("EMG", "긴급메시지", "나눔고딕", "", "red", 13),
    ERROR("ERR", "에러메시지", "나눔고딕", "", "red", 13),
    ;

    private String code;
    private String typeName;
    private String font;
    private String effect;
    private String color;
    private Integer fontSize;

    public static MsgType findByCode(String code) {
        return Arrays.stream(MsgType.values())
                .filter(e -> e.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}

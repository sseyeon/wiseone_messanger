package com.messanger.engine.uc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RequestAnswer {
    APPROVAL("A", "승인"),
    REJECT("R", "반려"),
    HOLD("H", "보류"),
    ;

    private String code;
    private String name;

    public static RequestAnswer findByCode(String code) {
        return Arrays.stream(RequestAnswer.values())
                .filter(e -> e.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}

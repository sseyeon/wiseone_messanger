package com.messanger.engine.uc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SecureType {
    USB("USB", "USB", "USB 보안"),
    ;
    private String name;
    private String code;
    private String desc;

    public static SecureType findByCode(String code) {
        return Arrays.stream(SecureType.values())
                .filter(e -> e.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}

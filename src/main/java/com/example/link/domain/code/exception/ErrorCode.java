package com.example.link.domain.code.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    CODE_NOT_FOUND("Code is not found"),

    CODE_ALREADY_EXPIRED("Code is already expired");
    private final String description;

}

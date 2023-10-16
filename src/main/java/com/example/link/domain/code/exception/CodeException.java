package com.example.link.domain.code.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public CodeException(CodeException.ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    @Getter
    @AllArgsConstructor
    public enum ErrorCode {
        CODE_NOT_FOUND("Code is not found"),

        CODE_ALREADY_EXPIRED("Code is already expired");
        private final String description;

    }

}

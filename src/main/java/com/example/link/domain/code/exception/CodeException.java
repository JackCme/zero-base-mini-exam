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
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    @Getter
    @AllArgsConstructor
    public enum ErrorCode {
        CODE_NOT_FOUND(404,"Code is not found")
        ,CODE_ALREADY_EXPIRED(400,"Code is already expired")
        ,CODE_INVALID(403,"Invalid code")
        ;
        private final int status;
        private final String description;

    }

}

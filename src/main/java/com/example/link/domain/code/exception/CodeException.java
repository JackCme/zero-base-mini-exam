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

    public CodeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

}

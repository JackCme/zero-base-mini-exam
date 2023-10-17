package com.example.link.domain.member.exception;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    @Getter
    @AllArgsConstructor
    public enum ErrorCode {
        MEMBER_NOT_FOUND(404,"Member is not found")
        ,EMAIL_NOT_VALID(400,"Email is not valid")
        ,PHONE_NUMBER_NOT_VALID(400,"Phone number is not valid")
        ,MEMBER_ALREADY_ACTIVE(400,"Member is already active")
        ;
        private final int status;
        private final String description;

    }


}
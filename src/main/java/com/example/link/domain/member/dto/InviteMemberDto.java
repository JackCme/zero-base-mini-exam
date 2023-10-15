package com.example.link.domain.member.dto;

import com.example.link.domain.code.dto.CodeDto;
import com.sun.istack.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class InviteMemberDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private String name;
        @NotNull
        private String phoneNumber;
        @NotNull
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String inviteCode;
        private LocalDateTime createdAt;
        public static Response from(CodeDto codeDto) {
            return Response.builder()
                    .inviteCode(codeDto.getInviteCode())
                    .createdAt(codeDto.getCreatedAt())
                    .build();
        }
    }
}

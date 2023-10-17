package com.example.link.domain.invite.dto;

import com.example.link.domain.code.dto.CodeDto;
import com.sun.istack.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class InviteMemberDto {
    @Getter
    @Setter
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
    @Builder
    public static class Response {
        private Long memberId;
        private String inviteCode;
        private LocalDateTime createdAt;
        public static Response from(CodeDto codeDto) {
            return InviteMemberDto.Response.builder()
                    .memberId(codeDto.getMember().getId())
                    .inviteCode(codeDto.getInviteCode())
                    .createdAt(codeDto.getCreatedAt())
                    .build();
        }
    }
}

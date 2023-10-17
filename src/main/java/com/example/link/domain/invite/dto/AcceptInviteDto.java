package com.example.link.domain.invite.dto;

import com.example.link.domain.member.dto.MemberDto;
import com.example.link.domain.member.type.MemberStatus;
import com.sun.istack.NotNull;
import lombok.*;

public class AcceptInviteDto {
    @Getter
    @Setter
    public static class Request {
        @NotNull
        private String inviteCode;
        @NotNull
        private Long memberId;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private Long memberId;
        private MemberStatus memberStatus;
        public static Response from(MemberDto memberDto) {
            return AcceptInviteDto.Response.builder()
                    .memberId(memberDto.getId())
                    .memberStatus(memberDto.getStatus())
                    .build();
        }

    }
}

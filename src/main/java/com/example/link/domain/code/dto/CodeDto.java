package com.example.link.domain.code.dto;

import com.example.link.domain.code.domain.Code;
import com.example.link.domain.member.domain.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeDto {
    private Long id;
    private String inviteCode;
    private Member member;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    public static CodeDto from(Code codeEntity) {
        return CodeDto.builder()
                .id(codeEntity.getId())
                .member(codeEntity.getMember())
                .inviteCode(codeEntity.getInviteCode())
                .createdAt(codeEntity.getCreatedAt())
                .expiredAt(codeEntity.getExpiredAt())
                .build();
    }

}

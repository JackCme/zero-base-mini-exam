package com.example.link.domain.code.dto;

import com.example.link.domain.code.domain.Code;
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

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    public static CodeDto from(Code codeEntity) {
        return CodeDto.builder()
                .id(codeEntity.getId())
                .inviteCode(codeEntity.getInviteCode())
                .createdAt(codeEntity.getCreatedAt())
                .expiredAt(codeEntity.getExpiredAt())
                .build();
    }
}

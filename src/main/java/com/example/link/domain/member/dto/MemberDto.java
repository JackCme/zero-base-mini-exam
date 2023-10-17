package com.example.link.domain.member.dto;

import com.example.link.domain.code.domain.Code;
import com.example.link.domain.code.dto.CodeDto;
import com.example.link.domain.member.domain.Member;
import com.example.link.domain.member.type.MemberStatus;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String email;
    private Collection<Code> codes;
    private Code lastCode;
    private MemberStatus status;

    public static MemberDto from(Member member) {
        if (member.getCodes() == null) {
            member.setCodes(List.of());
        }
        return MemberDto.builder()
                .id(member.getId())
                .codes(member.getCodes())
                .lastCode(member.getCodes().stream().max(Comparator.comparing(Code::getCodeSequence)).orElse(null))
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .status(member.getStatus())
                .build();
    }
}

package com.example.link.domain.invite.application;

import com.example.link.domain.code.application.CodeService;
import com.example.link.domain.code.dto.CodeDto;
import com.example.link.domain.member.application.MemberService;
import com.example.link.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final MemberService memberService;
    private final CodeService codeService;

    /**
     * 동일한 이메일의 회원이 테이블에 존재하는지 조회
     * 존재 하지만 아직 활성되지 않은 회원이면 기존 초대코드를 만료 후 초대코드를 신규 발급 및 회원정보에 업데이트
     * 존재 하지 않으면 비활성화 된 임시회원 생성 후 초대코드 신규 발급 및 회원정보 업데이트
     */
    @Transactional
    public CodeDto inviteMember(String name, String phoneNumber, String email) {
        MemberDto memberDto = memberService.createOrGetMemberByEmail(name, phoneNumber, email);
        memberService.validateMemberStatusInactive(memberDto);

        if (memberDto.getLastCode() != null) {
            codeService.expireMemberInviteCode(memberDto.getId(), memberDto.getLastCode().getInviteCode());
        }

        return codeService.generateMemberInviteCode(memberDto.getId());
    }

    @Transactional
    public MemberDto acceptInvite(Long memberId, String inviteCode) {
        MemberDto updatedMemberDto = memberService.activateMember(memberId);
        codeService.expireMemberInviteCode(memberId, inviteCode);
        return updatedMemberDto;
    }
}

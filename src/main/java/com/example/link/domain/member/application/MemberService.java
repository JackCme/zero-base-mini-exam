package com.example.link.domain.member.application;

import com.example.link.domain.code.application.CodeService;
import com.example.link.domain.code.dto.CodeDto;
import com.example.link.domain.member.dao.MemberRepository;
import com.example.link.domain.member.domain.Member;
import com.example.link.domain.member.type.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CodeService codeService;

    /**
     * 동일한 이메일의 회원이 테이블에 존재하는지 조회
     * 존재 하지만 아직 활성되지 않은 회원이면 기존 초대코드를 만료 후 초대코드를 신규 발급 및 회원정보에 업데이트
     * 존재 하지 않으면 비활성화 된 임시회원 생성 후 초대코드 신규 발급 및 회원정보 업데이트
     */
    @Transactional
    public CodeDto inviteMember(String name, String phoneNumber, String email) {
        CodeDto newInviteCodeDto = codeService.generateInviteCode();
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            memberRepository.save(
                    Member.builder()
                            .name(name).phoneNumber(phoneNumber).email(email)
                            .status(MemberStatus.INACTIVE)
                            .createdAt(LocalDateTime.now())
                            .modifiedAt(LocalDateTime.now())
                            .inviteCode(newInviteCodeDto.getInviteCode())
                            .build()
            );
            return newInviteCodeDto;
        }

        Member m = member.get();
        String oldInviteCode = m.getInviteCode();
        codeService.expireInviteCode(oldInviteCode);
        m.setInviteCode(newInviteCodeDto.getInviteCode());

        memberRepository.save(m);

        return newInviteCodeDto;

    }
}

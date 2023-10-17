package com.example.link.domain.code.application;

import com.example.link.domain.code.dao.CodeRepository;
import com.example.link.domain.code.domain.Code;
import com.example.link.domain.code.dto.CodeDto;
import com.example.link.domain.code.exception.CodeException;
import com.example.link.domain.code.type.CodeStatus;
import com.example.link.domain.member.dao.MemberRepository;
import com.example.link.domain.member.domain.Member;
import com.example.link.domain.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CodeService {
    private final CodeRepository codeRepository;
    private final MemberRepository memberRepository;

    /**
     * 새로운 초대코드를 생성 및 발급 한다.
     *
     * @return
     */
    @Transactional
    public CodeDto generateMemberInviteCode(Long memberId) {
        Long newCodeSequence = codeRepository.findFirstByOrderByIdDesc()
                .map(code -> Long.parseLong(code.getCodeSequence()) + 1)
                .orElse(1000000000L);
        String newInviteCode = CodeUtil.makeCodeFromSequence(newCodeSequence);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberException.ErrorCode.MEMBER_NOT_FOUND));

        Code savedCode = codeRepository.save(
                Code.builder()
                        .member(member)
                        .codeSequence(String.valueOf(newCodeSequence))
                        .codeStatus(CodeStatus.NORMAL)
                        .inviteCode(newInviteCode)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build()
        );

        return CodeDto.from(savedCode);
    }

    /**
     * 기존 생성된 초대코드를 만료시킨다
     */
    @Transactional
    public void expireMemberInviteCode(Long memberId, String inviteCode) {
        Code code = codeRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new CodeException(CodeException.ErrorCode.CODE_NOT_FOUND));

        validateExpiringCode(code, memberId);

        code.setCodeStatus(CodeStatus.EXPIRED);
        code.setExpiredAt(LocalDateTime.now());

        codeRepository.save(code);
    }

    private void validateExpiringCode(Code code, Long memberId) {
        if (CodeStatus.EXPIRED.equals(code.getCodeStatus())
                || code.getExpiredAt() != null) {
            throw new CodeException(CodeException.ErrorCode.CODE_ALREADY_EXPIRED);
        }
        if (!memberId.equals(code.getMember().getId())) {
            throw new CodeException(CodeException.ErrorCode.CODE_INVALID);
        }
    }
}

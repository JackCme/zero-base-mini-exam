package com.example.link.domain.member.application;

import com.example.link.domain.code.application.CodeService;
import com.example.link.domain.member.dao.MemberRepository;
import com.example.link.domain.member.domain.Member;
import com.example.link.domain.member.dto.MemberDto;
import com.example.link.domain.member.exception.MemberException;
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

    @Transactional
    public MemberDto createOrGetMemberByEmail(String name, String phoneNumber, String email) {
        if (!MemberUtil.isValidEmail(email)) throw new MemberException(MemberException.ErrorCode.EMAIL_NOT_VALID);
        if (!MemberUtil.isValidPhoneNumber(phoneNumber))
            throw new MemberException(MemberException.ErrorCode.PHONE_NUMBER_NOT_VALID);

        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            Member savedMember = memberRepository.save(Member.builder()
                    .name(name).phoneNumber(phoneNumber).email(email)
                    .status(MemberStatus.INACTIVE)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build()
            );
            return MemberDto.from(savedMember);
        }

        return MemberDto.from(member.get());
    }

    public void validateMemberStatusInactive(MemberDto memberDto) {
        if (!MemberStatus.INACTIVE.equals(memberDto.getStatus())) {
            throw new MemberException(MemberException.ErrorCode.MEMBER_ALREADY_ACTIVE);
        }
    }

    @Transactional
    public Member findMemberByIdOrThrow(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new MemberException(MemberException.ErrorCode.MEMBER_NOT_FOUND);
        }
        return member.get();
    }

    @Transactional
    public MemberDto activateMember(Long memberId) {
        Member member = findMemberByIdOrThrow(memberId);
        validateMemberStatusInactive(MemberDto.from(member));

        member.setStatus(MemberStatus.ACTIVE);
        Member updatedMember = memberRepository.save(member);

        return MemberDto.from(updatedMember);
    }
}

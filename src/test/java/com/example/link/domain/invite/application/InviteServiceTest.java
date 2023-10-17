package com.example.link.domain.invite.application;

import com.example.link.domain.code.application.CodeService;
import com.example.link.domain.code.application.CodeUtil;
import com.example.link.domain.code.domain.Code;
import com.example.link.domain.code.dto.CodeDto;
import com.example.link.domain.member.application.MemberService;
import com.example.link.domain.member.domain.Member;
import com.example.link.domain.member.dto.MemberDto;
import com.example.link.domain.member.type.MemberStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InviteServiceTest {
    @Mock
    private MemberService memberService;
    @Mock
    private CodeService codeService;

    @InjectMocks
    private InviteService inviteService;

    @Test
    @DisplayName("회원 초대 성공 테스트 - 신규 회원")
    void inviteMemberSuccess_NewMember() {
        // Given
        String name = "jack";
        String phoneNumber = " 01012341234";
        String email = "jack@example.com";
        Member member = Member.builder()
                .id(1L)
                .name(name).phoneNumber(phoneNumber).email(email)
                .status(MemberStatus.INACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        given(memberService.createOrGetMemberByEmail(name, phoneNumber, email))
                .willReturn(MemberDto.from(member));
        String inviteCode = CodeUtil.makeCodeFromSequence(1000000000L);
        given(codeService.generateMemberInviteCode(1L))
                .willReturn(CodeDto.builder()
                        .inviteCode(inviteCode)
                        .build());
        // When
        CodeDto codeDto = inviteService.inviteMember(name, phoneNumber, email);
        // Then
        assertEquals(inviteCode, codeDto.getInviteCode());
    }

    @Test
    @DisplayName("회원 초대 성공 테스트 - 존재하는 회원")
    void inviteMemberSuccess_ExistingMember() {
        // Given
        String name = "jack";
        String phoneNumber = " 01012341234";
        String email = "jack@example.com";
        Member member = Member.builder()
                .id(1L)
                .codes(List.of(
                        Code.builder().codeSequence("1000000000").inviteCode(CodeUtil.makeCodeFromSequence(1000000000L)).build(),
                        Code.builder().codeSequence("1000000001").inviteCode(CodeUtil.makeCodeFromSequence(1000000001L)).build()
                ))
                .name(name).phoneNumber(phoneNumber).email(email)
                .status(MemberStatus.INACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        given(memberService.createOrGetMemberByEmail(name, phoneNumber, email))
                .willReturn(MemberDto.from(member));
        String inviteCode = CodeUtil.makeCodeFromSequence(1000000001L);
        given(codeService.generateMemberInviteCode(1L))
                .willReturn(CodeDto.builder()
                        .inviteCode(inviteCode).build());

        // When
        CodeDto codeDto = inviteService.inviteMember(name, phoneNumber, email);
        // Then
        assertEquals(CodeUtil.makeCodeFromSequence(1000000001L), codeDto.getInviteCode());
    }

    @Test
    @DisplayName("초대 수락 성공 테스트 - 사용자의 상태가 활성되었다")
    void acceptInviteSuccess() {
        // Given
        given(memberService.activateMember(anyLong()))
                .willReturn(MemberDto.builder()
                        .status(MemberStatus.ACTIVE)
                        .build());
        String inviteCode = CodeUtil.makeCodeFromSequence(1000000000L);
        assertDoesNotThrow(() -> codeService.expireMemberInviteCode(1L, inviteCode));
        // When
        MemberDto memberDto = inviteService.acceptInvite(1L, inviteCode);
        // Then
        assertEquals(MemberStatus.ACTIVE, memberDto.getStatus());
    }

}
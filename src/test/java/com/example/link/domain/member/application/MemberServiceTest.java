package com.example.link.domain.member.application;

import com.example.link.domain.member.dao.MemberRepository;
import com.example.link.domain.member.domain.Member;
import com.example.link.domain.member.dto.MemberDto;
import com.example.link.domain.member.exception.MemberException;
import com.example.link.domain.member.type.MemberStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;


    @Test
    @DisplayName("임시 회원 생성 성공 테스트")
    void createOrGetMemberByEmail_CreateSuccess() {
        // Given
        String name = "jack";
        String email = "jack@example.com";
        String phone = "01012341234";
        Member savedMember = Member.builder()
                .name(name).phoneNumber(phone).email(email)
                .status(MemberStatus.INACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        given(memberRepository.save(any()))
                .willReturn(savedMember);

        // When
        MemberDto memberDto = memberService.createOrGetMemberByEmail(name, phone, email);
        // Then
        assertEquals(name, memberDto.getName());
        assertEquals(email, memberDto.getEmail());
        assertEquals(phone, memberDto.getPhoneNumber());
    }

    @Test
    @DisplayName("회원 조회 성공 테스트")
    void createOrGetMemberByEmail_GetSuccess() {
        // Given
        String name = "jack";
        String email = "jack@example.com";
        String phone = "01012341234";
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(Member.builder()
                        .name(name)
                        .email(email)
                        .phoneNumber(phone)
                        .build()));
        // When
        MemberDto memberDto = memberService.createOrGetMemberByEmail(name, phone, email);
        // Then
        assertEquals(name, memberDto.getName());
        assertEquals(email, memberDto.getEmail());
        assertEquals(phone, memberDto.getPhoneNumber());
    }

    @Test
    @DisplayName("회원 생성 조회 실패 테스트 - 유효하지 않은 이메일 주소 형식")
    void createOrGetMemberByEmailFail_InvalidEmail() {
        MemberException memberException =
                assertThrows(MemberException.class,
                        () -> memberService.createOrGetMemberByEmail("jack", "01012341234", "a@a@a"));
        assertEquals(MemberException.ErrorCode.EMAIL_NOT_VALID, memberException.getErrorCode());
        assertEquals("Email is not valid", memberException.getErrorMessage());
    }

    @Test
    @DisplayName("회원 생성 조회 실패 테스트 - 유효하지 않은 전화번호 형식")
    void createOrGetMemberByEmailFail_InvalidPhoneNumber() {
        MemberException memberException =
                assertThrows(MemberException.class,
                        () -> memberService.createOrGetMemberByEmail("jack", "1234", "jack@example.com"));
        assertEquals(MemberException.ErrorCode.PHONE_NUMBER_NOT_VALID, memberException.getErrorCode());
        assertEquals("Phone number is not valid", memberException.getErrorMessage());
    }

    @Test
    @DisplayName("회원 비활성화 상태 체크 성공 테스트")
    void validateMemberStatusInactive_Success() {
        // Given
        MemberDto memberDto = MemberDto.builder()
                .status(MemberStatus.INACTIVE)
                .build();
        // When
        assertDoesNotThrow(() -> memberService.validateMemberStatusInactive(memberDto), new MemberException(MemberException.ErrorCode.MEMBER_ALREADY_ACTIVE).getErrorMessage());
        // Then
    }

    @Test
    @DisplayName("회원 비활성화 상태 체크 실패 테스트")
    void validateMemberStatusInactive_Failed() {
        // Given
        MemberDto memberDto = MemberDto.builder()
                .status(MemberStatus.ACTIVE)
                .build();
        // When
        MemberException memberException = assertThrows(MemberException.class, () -> memberService.validateMemberStatusInactive(memberDto));

        // Then
        assertEquals(MemberException.ErrorCode.MEMBER_ALREADY_ACTIVE, memberException.getErrorCode());
        assertEquals(MemberException.ErrorCode.MEMBER_ALREADY_ACTIVE.getDescription(), memberException.getErrorMessage());
    }

    @Test
    @DisplayName("회원 ID 로 조회 성공 테스트")
    void findMemberByIdOrThrow_Success() {
        // Given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(1L).build()));
        // When
        Member member = memberService.findMemberByIdOrThrow(1L);
        // Then
        assertEquals(1L, member.getId());
    }

    @Test
    @DisplayName("회원 ID 로 조회 실패 테스트")
    void findMemberByIdOrThrow_Failed() {
        // Given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // When
        MemberException memberException = assertThrows(MemberException.class, () -> memberService.findMemberByIdOrThrow(1L));
        // Then
        assertEquals(MemberException.ErrorCode.MEMBER_NOT_FOUND, memberException.getErrorCode());
        assertEquals(MemberException.ErrorCode.MEMBER_NOT_FOUND.getDescription(), memberException.getErrorMessage());

    }

    @Test
    @DisplayName("회원 활성화 성공 테스트")
    void activateMemberSucess() {
        // Given
        Member member = Member.builder()
                .status(MemberStatus.INACTIVE)
                .build();
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(memberRepository.save(any()))
                .willReturn(member);

        // When
        MemberDto memberDto = memberService.activateMember(1L);

        // Then
        assertEquals(MemberStatus.ACTIVE, memberDto.getStatus());
    }

}
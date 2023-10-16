package com.example.link.domain.code.application;

import com.example.link.domain.code.dao.CodeRepository;
import com.example.link.domain.code.domain.Code;
import com.example.link.domain.code.dto.CodeDto;
import com.example.link.domain.code.exception.CodeException;
import com.example.link.domain.code.type.CodeStatus;
import com.example.link.domain.member.dao.MemberRepository;
import com.example.link.domain.member.domain.Member;
import com.example.link.domain.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CodeServiceTest {
    @Mock
    private CodeRepository codeRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private CodeService codeService;

    @Test
    @DisplayName("초대코드 발급 성공 테스트 - 최초 초대코드")
    void generateFirstInviteCodeSuccess() {
        // Given
        Long expectedSequence = 1000000000L;
        String expectedCode = CodeUtil.makeCodeFromSequence(expectedSequence);
        Long expectedMemberId = 1L;
        Member expectedMember = Member.builder()
                .id(expectedMemberId)
                .name("최원준")
                .email("pitou106@kakao.com")
                .phoneNumber("01012341234")
                .build();

        given(memberRepository.findById(expectedMemberId))
                .willReturn(Optional.of(expectedMember));
        given(codeRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.empty());
        given(codeRepository.save(any()))
                .willReturn(Code.builder()
                        .member(expectedMember)
                        .codeSequence(String.valueOf(expectedSequence))
                        .codeStatus(CodeStatus.NORMAL)
                        .inviteCode(expectedCode)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build());
        ArgumentCaptor<Code> captor = ArgumentCaptor.forClass(Code.class);

        // When
        CodeDto codeDto = codeService.generateMemberInviteCode(expectedMemberId);
        // Then
        verify(codeRepository, times(1)).save(captor.capture());
        assertEquals(expectedMember, captor.getValue().getMember());
        assertEquals(expectedCode, codeDto.getInviteCode());
        assertEquals(expectedCode, captor.getValue().getInviteCode());
        assertEquals(String.valueOf(expectedSequence), captor.getValue().getCodeSequence());
        assertEquals(CodeStatus.NORMAL, captor.getValue().getCodeStatus());
    }

    @Test
    @DisplayName("초대코드 발급 성공 테스트 - 기존 초대코드 존재할때")
    void generateInviteCodeSuccess() {
        // Given
        Long foundSequence = 1000000010L;
        Code code = Code.builder()
                .codeSequence(String.valueOf(foundSequence))
                .build();
        Long expectedSequence = foundSequence + 1;
        String expectedCode = CodeUtil.makeCodeFromSequence(expectedSequence);
        Long expectedMemberId = 1L;
        Member expectedMember = Member.builder()
                .id(expectedMemberId)
                .name("최원준")
                .email("pitou106@kakao.com")
                .phoneNumber("01012341234")
                .build();

        given(memberRepository.findById(expectedMemberId))
                .willReturn(Optional.of(expectedMember));
        given(codeRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.of(code));
        given(codeRepository.save(any()))
                .willReturn(Code.builder()
                        .member(expectedMember)
                        .codeSequence(String.valueOf(expectedSequence))
                        .codeStatus(CodeStatus.NORMAL)
                        .inviteCode(expectedCode)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build());
        ArgumentCaptor<Code> captor = ArgumentCaptor.forClass(Code.class);

        // When
        CodeDto codeDto = codeService.generateMemberInviteCode(1L);
        // Then
        verify(codeRepository, times(1)).save(captor.capture());
        assertEquals(expectedMember, captor.getValue().getMember());
        assertEquals(expectedCode, codeDto.getInviteCode());
        assertEquals(expectedCode, captor.getValue().getInviteCode());
        assertEquals(String.valueOf(expectedSequence), captor.getValue().getCodeSequence());
        assertEquals(CodeStatus.NORMAL, captor.getValue().getCodeStatus());
        assertNull(captor.getValue().getExpiredAt());
    }

    @Test
    @DisplayName("초대코드 발급 실패 테스트 - 사용자 존재하지 않음")
    void generateInviteCodeFailed_NoMember() {
        // Given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        // When
        MemberException memberException = assertThrows(MemberException.class, () -> codeService.generateMemberInviteCode(0L));
        // Then
        assertEquals(MemberException.ErrorCode.MEMBER_NOT_FOUND, memberException.getErrorCode());
        assertEquals("Member is not found", memberException.getErrorMessage());
    }

    @Test
    @DisplayName("코드 만료 실패 테스트 - 해당하는 초대코드 없음")
    void expireInviteCodeNotFound() {
        // Given
        given(codeRepository.findByInviteCode(anyString()))
                .willReturn(Optional.empty());
        // When
        CodeException codeException = assertThrows(CodeException.class, () -> codeService.expireInviteCode("1234"));

        // Then
        assertEquals(CodeException.ErrorCode.CODE_NOT_FOUND, codeException.getErrorCode());
        assertEquals("Code is not found", codeException.getErrorMessage());
    }

    @Test
    @DisplayName("코드 만료 실패 테스트 - 해당 코드 이미 만료됨")
    void expireInviteCodeAlreadyExpired() {
        // Given
        Long codeSequence = 1000000010L;
        String inviteCode = CodeUtil.makeCodeFromSequence(codeSequence);
        Code code = Code.builder()
                .codeSequence(String.valueOf(codeSequence))
                .codeStatus(CodeStatus.EXPIRED)
                .inviteCode(inviteCode)
                .expiredAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        given(codeRepository.findByInviteCode(anyString()))
                .willReturn(Optional.of(code));
        // When
        CodeException codeException = assertThrows(CodeException.class, () -> codeService.expireInviteCode(inviteCode));

        // Then
        assertEquals(CodeException.ErrorCode.CODE_ALREADY_EXPIRED, codeException.getErrorCode());
        assertEquals("Code is already expired", codeException.getErrorMessage());
    }

    @Test
    @DisplayName("코드 만료 성공 테스트")
    void expireInviteCodeSuccess() {
        // Given
        Long codeSequence = 1000000010L;
        String inviteCode = CodeUtil.makeCodeFromSequence(codeSequence);
        Code code = Code.builder()
                .codeSequence(String.valueOf(codeSequence))
                .codeStatus(CodeStatus.NORMAL)
                .inviteCode(inviteCode)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        given(codeRepository.findByInviteCode(anyString()))
                .willReturn(Optional.of(code));

        ArgumentCaptor<Code> captor = ArgumentCaptor.forClass(Code.class);
        // When
        codeService.expireInviteCode(inviteCode);
        // Then
        verify(codeRepository, times(1)).save(captor.capture());
        assertEquals(CodeStatus.EXPIRED, captor.getValue().getCodeStatus());
        assertNotNull(captor.getValue().getExpiredAt());
    }
}
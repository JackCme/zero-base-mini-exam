package com.example.link.domain.invite.api;

import com.example.link.domain.code.exception.CodeException;
import com.example.link.domain.invite.application.InviteService;
import com.example.link.domain.invite.dto.AcceptInviteDto;
import com.example.link.domain.invite.dto.InviteMemberDto;
import com.example.link.domain.member.exception.MemberException;
import com.example.link.global.error.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invite")
public class InviteController {
    private final InviteService inviteService;

    @ExceptionHandler({CodeException.class})
    protected ResponseEntity<ErrorDto> handleCodeException(CodeException ex) {
        return new ResponseEntity<>(
                new ErrorDto(ex.getErrorCode().getStatus(), ex.getErrorCode().getDescription()),
                HttpStatus.valueOf(ex.getErrorCode().getStatus())
        );
    }

    @ExceptionHandler({MemberException.class})
    protected ResponseEntity<ErrorDto> handleMemberException(MemberException ex) {
        return new ResponseEntity<>(
                new ErrorDto(ex.getErrorCode().getStatus(), ex.getErrorCode().getDescription()),
                HttpStatus.valueOf(ex.getErrorCode().getStatus())
        );
    }

    @PostMapping("/member")
    public InviteMemberDto.Response inviteMember(
            @RequestBody @Valid InviteMemberDto.Request request
    ) {
        return InviteMemberDto.Response.from(
                inviteService.inviteMember(request.getName(), request.getPhoneNumber(), request.getEmail())
        );
    }

    @PostMapping("/accept")
    public AcceptInviteDto.Response acceptInvite(
            @RequestBody @Valid AcceptInviteDto.Request request
    ) {
        return AcceptInviteDto.Response.from(
                inviteService.acceptInvite(request.getMemberId(), request.getInviteCode())
        );
    }
}

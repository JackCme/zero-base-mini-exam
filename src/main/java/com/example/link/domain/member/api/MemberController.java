package com.example.link.domain.member.api;

import com.example.link.domain.member.application.MemberService;
import com.example.link.domain.member.dto.InviteMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/invite")
    public InviteMemberDto.Response inviteMember(
            @RequestBody @Valid InviteMemberDto.Request request
    ) {
        return InviteMemberDto.Response.from(memberService.inviteMember(
                request.getName(), request.getPhoneNumber(), request.getEmail()
        ));
    }
}

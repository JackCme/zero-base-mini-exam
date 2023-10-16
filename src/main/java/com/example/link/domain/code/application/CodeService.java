package com.example.link.domain.code.application;

import com.example.link.domain.code.dao.CodeRepository;
import com.example.link.domain.code.domain.Code;
import com.example.link.domain.code.dto.CodeDto;
import com.example.link.domain.code.exception.CodeException;
import com.example.link.domain.code.exception.ErrorCode;
import com.example.link.domain.code.type.CodeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CodeService {
    private final CodeRepository codeRepository;

    /**
     * 새로운 초대코드를 생성 및 발급 한다.
     *
     * @return
     */
    @Transactional
    public CodeDto generateInviteCode() {
        String newCodeSequence = String.valueOf(codeRepository.findFirstByOrderByIdDesc()
                .map(code -> Integer.parseInt(code.getCodeSequence()) + 1)
                .orElse(1000000000));
        String newInviteCode = CodeUtil.makeCodeFromSequence(Long.parseLong(newCodeSequence));

        Code savedCode = codeRepository.save(
                Code.builder()
                        .codeSequence(newCodeSequence)
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
    public void expireInviteCode(String inviteCode) {
        Code code = codeRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new CodeException(ErrorCode.CODE_NOT_FOUND));

        code.setCodeStatus(CodeStatus.EXPIRED);
        code.setExpiredAt(LocalDateTime.now());

        codeRepository.save(code);
    }
}

package com.example.link.domain.code.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeUtilTest {

    @Test
    @DisplayName("시퀀스에서 코드를 의도대로 생성하는지 테스트")
    void makeCodeFromSequence() {
        // Given
        Long sequence = 1000000000L;
        // When
        String codeFromSequence = CodeUtil.makeCodeFromSequence(sequence);
        // Then
        assertEquals("015FTGg", codeFromSequence);
    }
}
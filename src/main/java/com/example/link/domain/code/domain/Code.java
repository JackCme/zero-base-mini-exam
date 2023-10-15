package com.example.link.domain.code.domain;

import com.example.link.domain.code.type.CodeStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = @Index(
        name = "idx_code",
        columnList = "invite_code",
        unique = true
))
public class Code {
    @Id
    @GeneratedValue
    private Long id;
    private Long codeSequence;
    private String inviteCode;
    @Enumerated(EnumType.STRING)
    private CodeStatus codeStatus;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    private LocalDateTime expiredAt;
}

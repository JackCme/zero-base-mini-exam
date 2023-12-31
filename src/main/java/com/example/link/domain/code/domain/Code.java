package com.example.link.domain.code.domain;

import com.example.link.domain.code.type.CodeStatus;
import com.example.link.domain.member.domain.Member;
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
        columnList = "inviteCode",
        unique = true
))
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    private String codeSequence;
    private String inviteCode;
    @Enumerated(EnumType.STRING)
    private CodeStatus codeStatus;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    private LocalDateTime expiredAt;
}

package com.example.link.domain.member.dao;

import com.example.link.domain.member.type.MemberStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MemberDao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

}

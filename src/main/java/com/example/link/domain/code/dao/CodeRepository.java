package com.example.link.domain.code.dao;

import com.example.link.domain.code.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    Optional<Code> findFirstByOrderByIdDesc();

    Optional<Code> findByInviteCode(String inviteCode);
}

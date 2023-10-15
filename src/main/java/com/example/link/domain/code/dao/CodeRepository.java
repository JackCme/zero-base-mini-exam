package com.example.link.domain.code.dao;

import com.example.link.domain.code.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<Code, Long> {
}

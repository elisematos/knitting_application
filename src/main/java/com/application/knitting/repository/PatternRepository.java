package com.application.knitting.repository;

import com.application.knitting.model.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatternRepository extends JpaRepository<Pattern, Long> {
}

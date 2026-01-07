package com.redbook.repository;

import com.redbook.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByPhoneAndCodeAndType(String phone, String code, String type);
    List<VerificationCode> findByPhoneAndExpiredAtAfter(String phone, java.time.LocalDateTime expiredAt);
}
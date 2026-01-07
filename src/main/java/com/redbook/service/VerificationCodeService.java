package com.redbook.service;

import com.redbook.entity.VerificationCode;
import com.redbook.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationCodeService {
    
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    public String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public boolean saveCode(String phone, String code, String type) {
        // 删除旧的验证码
        deleteOldCodes(phone, type);
        
        // 创建新的验证码，5分钟有效期
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhone(phone);
        verificationCode.setCode(code);
        verificationCode.setType(type);
        verificationCode.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        
        verificationCodeRepository.save(verificationCode);
        return true;
    }

    public boolean verifyCode(String phone, String code, String type) {
        // 查找未过期的验证码
        var codes = verificationCodeRepository.findByPhoneAndExpiredAtAfter(phone, LocalDateTime.now());
        
        for (VerificationCode vc : codes) {
            if (vc.getCode().equals(code) && vc.getType().equals(type) && vc.getStatus() == 0) {
                // 标记为已使用
                vc.setStatus(1);
                verificationCodeRepository.save(vc);
                return true;
            }
        }
        return false;
    }

    private void deleteOldCodes(String phone, String type) {
        // 删除指定手机号和类型的过期验证码
        var oldCodes = verificationCodeRepository.findByPhoneAndExpiredAtAfter(phone, LocalDateTime.now().minusMinutes(5));
        for (var code : oldCodes) {
            if (code.getType().equals(type)) {
                verificationCodeRepository.delete(code);
            }
        }
    }
}
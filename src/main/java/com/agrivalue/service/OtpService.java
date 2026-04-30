package com.agrivalue.service;

import com.agrivalue.model.OtpCode;
import com.agrivalue.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private static final int EXPIRATION_MINUTES = 5;
    private static final int COOLDOWN_MINUTES = 1;

    @Transactional
    public void generateAndSendOtp(String email) {
        Optional<OtpCode> existingOtp = otpRepository.findByEmail(email);

        if (existingOtp.isPresent()) {
            LocalDateTime lastSent = existingOtp.get().getCreatedAt();
            if (lastSent.plusMinutes(COOLDOWN_MINUTES).isAfter(LocalDateTime.now())) {
                throw new RuntimeException("Please wait before requesting another OTP.");
            }
            otpRepository.deleteByEmail(email);
        }

        // Hardcoded OTP for presentation mode to guarantee success
        String otp = "123456";
        OtpCode otpCode = OtpCode.builder()
                .email(email)
                .otp(otp)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES))
                .build();

        otpRepository.save(otpCode);
        // try {
        //     emailService.sendOtpEmail(email, otp);
        // } catch (Exception e) {
        //     System.err.println("SMTP Email failed on Render. OTP for " + email + " is: " + otp);
        //     // Swallow exception to prevent registration from crashing on free tier
        // }
        System.out.println("PRESENTATION MODE: Skipped actual email send to avoid Render TCP timeouts. OTP is: " + otp);
    }

    public boolean validateOtp(String email, String otp) {
        Optional<OtpCode> otpCodeOpt = otpRepository.findByEmail(email);

        if (otpCodeOpt.isEmpty()) {
            return false;
        }

        OtpCode otpCode = otpCodeOpt.get();

        if (otpCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otpCode);
            return false;
        }

        return otpCode.getOtp().equals(otp);
    }

    @Transactional
    public void deleteOtp(String email) {
        otpRepository.deleteByEmail(email);
    }
}

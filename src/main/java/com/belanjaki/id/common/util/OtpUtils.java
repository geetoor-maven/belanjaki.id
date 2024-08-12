package com.belanjaki.id.common.util;

import java.security.SecureRandom;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class OtpUtils {

    private static final int DIGIT_OTP = 6;

    public static String generateOtp(){

        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(DIGIT_OTP);

        for (int i = 0; i < DIGIT_OTP; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();

    }

    public static boolean isMoreThanOneMinute(Timestamp lastUpdatedDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime theUpdatedDate = LocalDateTime.parse(lastUpdatedDate.toString(), formatter);
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(theUpdatedDate, now);
        return minutes > 1;
    }
}

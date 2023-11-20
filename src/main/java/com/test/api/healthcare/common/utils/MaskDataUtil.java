package com.test.api.healthcare.common.utils;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = PRIVATE)
public class MaskDataUtil {

    public static String maskDate(final String date) {
        return date.replaceAll("(?<=.{4})[\\d]", "*");

    }

    public static String maskData(final String input) {
        final int maskNumber = 4;
        if (input.length() > maskNumber) {
            return input.replaceAll("[^-](?=.{4})", "*");
        } else {
            return input.replaceAll("[^-](?=.{4})", "*");
        }
    }

    public static String maskEmail(final String email) {
        final Pattern pattern = Pattern.compile("^(.)(.*)(.{2})@(.*)(\\..{2,4})$");
        final int group4 = 4;
        final int group5 = 5;
        if (email == null || email.isEmpty()) {
            return email;
        }
        final Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
            final String maskedUsername = matcher.group(1) + "*".repeat(matcher.group(2).length()) + matcher.group(3);
            return maskedUsername + "@" + matcher.group(group4) + matcher.group(group5);
        }
        return email;
    }
}

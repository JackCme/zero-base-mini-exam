package com.example.link.domain.code.application;

public class CodeUtil {
    public static String makeCodeFromSequence(Long sequence) {
        String elements = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        while (sequence != 0) {
            sb.insert(0, elements.charAt((int) (sequence % 62)));
            sequence /= 62;
        }
        while (sb.length() != 7) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }
}

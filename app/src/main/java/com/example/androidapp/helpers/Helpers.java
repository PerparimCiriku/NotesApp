package com.example.androidapp.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Helpers {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static String hashPassword(String password) {
        if (password == null) {
            throw new NullPointerException("Password cannot be null");
        }
        if (password.isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes = messageDigest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage());
        }
    }


    public static boolean isValidEmail(String emailStr) {
        if (emailStr == null) {
            return false;  // Return false if email is null
        }
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

}

package com.example.androidapp;

import com.example.androidapp.helpers.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelpersTest {

    // Test for valid email
    @Test
    public void testIsValidEmail_ValidEmail() {
        String email = "test@example.com";
        assertTrue(Helpers.isValidEmail(email));
    }

    // Test for invalid email (invalid format)
    @Test
    public void testIsValidEmail_InvalidEmail() {
        String email = "invalid-email";
        assertFalse(Helpers.isValidEmail(email));
    }

    // Test for null email
    @Test
    public void testIsValidEmail_NullEmail() {
        String email = null;
        assertFalse(Helpers.isValidEmail(email));  // Expecting false for null email
    }

    // Test for hashing a password - ensuring consistent output for the same password
    @Test
    public void testHashPassword_ConsistentHash() {
        String password = "Password123!";
        String hash1 = Helpers.hashPassword(password);
        String hash2 = Helpers.hashPassword(password);

        // Ensure that the same password results in the same hash each time
        assertEquals("Hashes should be identical for the same input password", hash1, hash2);
    }

    // Test for hashing different passwords - ensuring different hashes for different inputs
    @Test
    public void testHashPassword_DifferentPasswords() {
        String password1 = "Password123!";
        String password2 = "DifferentPassword456!";

        String hash1 = Helpers.hashPassword(password1);
        String hash2 = Helpers.hashPassword(password2);

        // Ensure that different passwords result in different hashes
        assertNotEquals("Hashes should be different for different passwords", hash1, hash2);
    }

    // Test for null or empty input (this will test how the function handles an empty password)
    @Test(expected = RuntimeException.class)
    public void testHashPassword_EmptyPassword() {
        String password = "";
        Helpers.hashPassword(password);  // Expected to hash but let's see if it handles empty passwords well
    }

    // Test for null input (this checks that the function correctly throws an exception for null input)
    @Test(expected = NullPointerException.class)
    public void testHashPassword_NullPassword() {
        String password = null;
        Helpers.hashPassword(password);  // This will now throw a NullPointerException if the password is null
    }
}

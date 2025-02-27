package com.example.androidapp;

import com.example.androidapp.helpers.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelpersTest {


    @Test
    public void testIsValidEmail_ValidEmail() {
        String email = "test@example.com";
        assertTrue(Helpers.isValidEmail(email));
    }




    @Test
    public void testIsValidEmail_InvalidEmail() {
        String email = "invalid-email";
        assertFalse(Helpers.isValidEmail(email));
    }


    @Test
    public void testIsValidEmail_NullEmail() {
        String email = null;
        assertFalse(Helpers.isValidEmail(email));
    }


    @Test
    public void testHashPassword_ConsistentHash() {
        String password = "Password123!";
        String hash1 = Helpers.hashPassword(password);
        String hash2 = Helpers.hashPassword(password);


        assertEquals("Hashes should be identical for the same input password", hash1, hash2);
    }


    @Test
    public void testHashPassword_DifferentPasswords() {
        String password1 = "Password123!";
        String password2 = "DifferentPassword456!";

        String hash1 = Helpers.hashPassword(password1);
        String hash2 = Helpers.hashPassword(password2);


        assertNotEquals("Hashes should be different for different passwords", hash1, hash2);
    }


    @Test(expected = RuntimeException.class)
    public void testHashPassword_EmptyPassword() {
        String password = "";
        Helpers.hashPassword(password);
    }


    @Test(expected = NullPointerException.class)
    public void testHashPassword_NullPassword() {
        String password = null;
        Helpers.hashPassword(password);
    }
}

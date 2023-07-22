package com.prgrms.gccoffee.model;

import com.prgrms.gccoffee.common.model.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @Test
    void testEmail() {
        assertThrows(IllegalArgumentException.class, () ->{
            Email email = new Email("test");
        });
    }

    @Test
    void testValidEmail() {
        Email email1 = new Email("hello@gmail.com");
        Email email2 = new Email("hello@gmail.com");

        assertEquals(email1, email2);
    }
}
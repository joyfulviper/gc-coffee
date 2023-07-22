package com.prgrms.gccoffee.common.model;

import org.springframework.util.Assert;

import java.util.Objects;

public class Email {
    private final String address;

    public Email(String address) {
        Assert.notNull(address, "address should not be null");
        Assert.isTrue(address.length() >= 4 && address.length() <= 50, "address length must be between 4 and 50 characters");
        Assert.isTrue(checkAddress(address), "Invalid email address");
        this.address = address;
    }

    private static boolean checkAddress(String address) {
        return address.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
package com.todo.app.security.util.enums;


public enum KeyLength {
    REFRESH_NAME(30),
    JWT_KEY(50),
    REFRESH(100),
    SALT(512);

    private final int length;

    KeyLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}

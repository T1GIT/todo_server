package com.todo.app.security;

import java.util.regex.Pattern;


/**
 * Provides static methods to check if
 * email or password string is valid
 */
public abstract class Validator {

    /**
     * RegExp for validating email address
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,63})$");

    /**
     * RegExp for validating password
     */
    private static final Pattern PSW_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-zA-Zа-яА-Я]).*$");

    /**
     * Checks if email is valid
     * <p>
     * Email must:
     * <ul>
     * <li> contain only latin symbols and numerics
     * <li> contain al least 3 domains, separated by point
     * </ul>
     *
     * @param email email address string
     * @return true if email is valid
     */
    public static boolean email(final String email) {
        return EMAIL_PATTERN.matcher(email).matches() &&
                email.length() >= 7 &&
                email.length() <= 255;
    }

    /**
     * Checks if password is valid
     * <p>
     * Password must:
     * <ul>
     * <li> contain numerics
     * <li> contain latin or cyrillic symbols
     * <li> be longer then 7 symbols
     * <li> be shorter then 121 symbols
     * </ul>
     *
     * @param psw raw password string
     * @return true if password is invalid
     */
    public static boolean psw(final String psw) {
        return PSW_PATTERN.matcher(psw).matches() &&
                psw.length() >= 8 &&
                psw.length() <= 120;
    }

}
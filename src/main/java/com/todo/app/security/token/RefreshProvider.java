package com.todo.app.security.token;

import com.todo.app.api.util.CookieUtil;
import com.todo.app.security.util.exception.MissedRefreshException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;


public abstract class RefreshProvider {

    private final static Duration DURATION = Duration.ofDays(30);
    private final static String TOKEN_NAME = "REFRESH";

    public static void attach(HttpServletResponse response, String refresh) {
        CookieUtil.add(response, TOKEN_NAME, refresh, DURATION.getSeconds());
    }

    public static String extract(HttpServletRequest request) throws MissedRefreshException {
        String refresh = CookieUtil.get(request, TOKEN_NAME);
        if (refresh == null)
            throw new MissedRefreshException();
        return refresh;
    }

    public static void erase(HttpServletResponse response) {
        CookieUtil.add(response, TOKEN_NAME, null, 0);
    }
}
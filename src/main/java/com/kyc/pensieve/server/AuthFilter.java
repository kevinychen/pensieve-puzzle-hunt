package com.kyc.pensieve.server;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthFilter implements Filter {

    public static String COOKIE_NAME = "TEAM_XXXXXXX_PUZZLES";
    public static String ENTRANCE = "/entrance.html";

    private static ThreadLocal<String> accountThreadLocal = new ThreadLocal<>();

    public static String getAccount() {
        return accountThreadLocal.get();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (canContinue((HttpServletRequest) request))
            chain.doFilter(request, response);
        else
            ((HttpServletResponse) response).sendRedirect(ENTRANCE);
    }

    private static boolean canContinue(HttpServletRequest request) {
        String path = request.getRequestURI();
        Optional<String> cookie = findCookie(request);
        if (cookie.isPresent() && path.startsWith("/api"))
            accountThreadLocal.set(cookie.get());
        if (cookie.isPresent())
            return true;
        return !path.equals("/") && (path.equals(ENTRANCE) || !path.endsWith(".html"));
    }

    private static Optional<String> findCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return Optional.empty();
        return Stream.of(cookies)
            .filter(cookie -> cookie.getName().equalsIgnoreCase(COOKIE_NAME))
            .map(Cookie::getValue)
            .findFirst()
            .flatMap(cookie -> PensieveFiles.getState().getAccounts().containsKey(cookie) ? Optional.of(cookie) : Optional.empty());
    }

    @Override
    public void destroy() {
    }
}

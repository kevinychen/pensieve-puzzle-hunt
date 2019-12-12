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

    private boolean canContinue(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (!path.equals("/") && (path.equals(ENTRANCE) || !path.endsWith(".html")))
            return true;
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return false;
        Optional<String> pensieveCookie = Stream.of(cookies)
                .filter(cookie -> cookie.getName().equalsIgnoreCase(COOKIE_NAME))
                .map(cookie -> cookie.getValue().toUpperCase())
                .findFirst();
        return pensieveCookie.isPresent() && PensieveFiles.getConfig().getAccounts().contains(pensieveCookie.get());
    }

    @Override
    public void destroy() {
    }
}

package com.keepyuppy.KeepyUppy.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtils.resolveToken(request);
        if (token != null && jwtUtils.verifyToken(token, true)) {
            Authentication authentication = jwtUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private static List<String> skipFilterUrls = Arrays.asList(
            "/api/user/test/**",
            "/api/auth/refresh",
            "/login/**",
            "/swagger**", "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return skipFilterUrls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
    }

}

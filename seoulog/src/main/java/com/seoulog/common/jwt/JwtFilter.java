package com.seoulog.common.jwt;

import com.seoulog.common.error.BusinessException;
import com.seoulog.common.error.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final int tokenValidityInMilliseconds = 86400;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String COOKIE_NAME = "refresh-token";
    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        String accessToken = resolveToken(authorizationHeader);

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken).equals("ACCESS")) { //토큰 검증
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken).equals("Expired")) {
            String refreshToken = null;
            refreshToken = getRefreshToken(request, refreshToken);
                //refreshToken 검증
            if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken).equals("ACCESS")) {

                // access token 재발급
                Authentication authentication = tokenProvider.getAuthentication(refreshToken);
                String newAccessToken = tokenProvider.createAccessToken(authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                response.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);
            }
        } else {
            throw new BusinessException(ErrorCode.TOKEN_EMPTY);
        }

        filterChain.doFilter(request, response);
        }


    private String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER)) {
            return token.substring(7);
        }
        return null;
    }

    private String getRefreshToken(HttpServletRequest request, String refreshToken) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_NAME)) {
                refreshToken = cookie.getValue();
            }
        }
        return refreshToken;
    }
}



package com.seoulog.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        final String authorizationHeader = request.getHeader("Authorization");
        String accessToken = resolveToken(authorizationHeader);

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken).equals("ACCESS")) { //토큰 검증
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken).equals("Expired")) {
            String refreshToken = null;
            if (StringUtils.hasText(request.getHeader("Auth"))) { // Auth에는 email 담겨 있음
                String email = request.getHeader("Auth");
                refreshToken = tokenProvider.getRefreshToken(email); // userId로 refreshToken 조회
            }
        } else {
                logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(request, response);
        }


    private String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

}



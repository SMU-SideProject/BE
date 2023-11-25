package com.seoulog.seoulog.jwt;

import com.seoulog.seoulog.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends GenericFilterBean {

    private final int tokenValidityInMilliseconds = 86400;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String accessToken = resolveToken(httpServletRequest); //request header에서 token값 가져오기
        String requestURI = httpServletRequest.getRequestURI();
        System.out.println("accessToken, = " + accessToken);

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken).equals("ACCESS")) { //토큰 검증
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken).equals("Expired")) {
            log.info("Access token expired");
            String refreshToken = null;
            if (StringUtils.hasText(httpServletRequest.getHeader("Auth"))) { // Auth에는 email 담겨 있음
                String email = httpServletRequest.getHeader("Auth");
                refreshToken = tokenProvider.getRefreshToken(email); // userId로 refreshToken 조회
            }
        } else {
                logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
        }

//    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}



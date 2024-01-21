package com.seoulog.user.jwt;

import com.seoulog.common.error.BusinessException;
import com.seoulog.common.error.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    /**
     * 토큰 관련 에러 핸들링
     * JwtTokenFilter 에서 발생하는 에러를 핸들링해준다.
     * <토큰의 유효성 검사>
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // 다음 filter Chain에 대한 실행 (filter-chain의 마지막에는 Dispatcher Servlet이 실행된다.)
            filterChain.doFilter(request, response); //got to JwtFilter
        } catch (BusinessException e) {
            setErrorResponse(e.getErrorCode(), response);
        }
    }

    /**
     * Security Chain 에서 발생하는 에러 응답 구성
     */
    public void setErrorResponse(ErrorCode errorCode, HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", errorCode.getMessage());
        responseJson.put("status", errorCode.getStatus().value());
        response.setStatus(errorCode.getStatus().value());

        response.getWriter().print(responseJson);

    }

}

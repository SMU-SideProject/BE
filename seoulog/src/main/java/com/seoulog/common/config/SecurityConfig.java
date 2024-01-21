package com.seoulog.common.config;

import com.seoulog.user.exception.JwtAccessDeniedHandler;
import com.seoulog.user.exception.JwtAuthenticationEntryPoint;
import com.seoulog.user.jwt.JwtSecurityConfig;
import com.seoulog.user.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    //TokenProvider 주입 받아서 JwtFilter를 통해 Security 로직에 필터 등록

    private final TokenProvider tokenProvider;
    //    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf(csrf -> csrf.disable());

        http.cors(Customizer.withDefaults());

        http
                .authorizeHttpRequests(auth ->
                        auth
                            .requestMatchers(new AntPathRequestMatcher("/users/signup/**")).permitAll() //회원 가입을 위한 api (토큰이 없는 상태로 요청이 오므로 permitAll()
                                    .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                                    .requestMatchers(new AntPathRequestMatcher("/favicon.ico")).permitAll()
                                    .requestMatchers(new AntPathRequestMatcher("/users/login/**")).permitAll()
                                    .requestMatchers(new AntPathRequestMatcher("/file/**")).permitAll()
                                    .requestMatchers(new AntPathRequestMatcher("/test/**")).permitAll()
                                    .anyRequest().permitAll());

        http
                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // enable h2-console
                .headers(headers ->
                        headers.frameOptions(options ->
                                options.sameOrigin()
                        )
                )
                .apply(new JwtSecurityConfig(tokenProvider));
        return http.build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
                .requestMatchers(new AntPathRequestMatcher("/users/login/**"))
                .requestMatchers(new AntPathRequestMatcher("/users/signup/**"))
                .requestMatchers(new AntPathRequestMatcher("/test/**"));

    }
}

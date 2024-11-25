package org.zerock.boardex_1.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.boardex_1.security.CustomUserDetailsService;
import org.zerock.boardex_1.security.handler.Custom403Handler;
import org.zerock.boardex_1.security.handler.CustomSocialLoginSuccessHandler;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
// @PreAuthorize 혹은 @PostAuthorize 어노테이션을 이용해서 사전 혹은 사후의 권한 체크
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("-------------configure--------------");

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/member/login", "/member/join", "/static/**").permitAll() // 공개적으로 허용할 URL
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/member/login")
                        .failureUrl("/member/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                ).logout(logout -> logout
                        .logoutUrl("/member/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/member/login?logout=true") // 로그아웃 성공 후 이동할 URL
                        .deleteCookies("JSESSIONID", "remember-me") // 삭제할 쿠키 지정
                        .invalidateHttpSession(true) // 세션 무효화
                        .clearAuthentication(true) // 인증 정보 제거
                )
                .rememberMe(rm -> rm
                        .key("12345678")
                        .tokenValiditySeconds(60 * 60 * 24 * 30)
                        .tokenRepository(persistentTokenRepository()))
                .userDetailsService(customUserDetailsService)
                .exceptionHandling(e -> e
                        .accessDeniedHandler(accessDeniedHandler()))
                .oauth2Login(oauth -> oauth
                        .loginPage("/member/login")
                        .successHandler(authenticationSuccessHandler()))
                .build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("-------------configure--------------");

        return (web) -> web.ignoring().requestMatchers(PathRequest
                .toStaticResources().atCommonLocations());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }
}

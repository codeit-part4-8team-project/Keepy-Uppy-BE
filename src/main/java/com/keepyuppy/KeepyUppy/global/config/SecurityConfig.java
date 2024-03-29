package com.keepyuppy.KeepyUppy.global.config;

import com.keepyuppy.KeepyUppy.security.jwt.JwtAuthenticationFilter;
import com.keepyuppy.KeepyUppy.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtils jwtUtils;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    // TODO: Update allowed origins with actual url
                    config.setAllowedOrigins(List.of("http://localhost:8080"));
                    config.setAllowedOrigins(List.of("http://localhost:5173"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))

                .authorizeHttpRequests((request -> request
                        .requestMatchers(new AntPathRequestMatcher("/test/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/login/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/oauth/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/auth/refresh")).permitAll()
                        .requestMatchers( "/","/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**","/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(config->config
                        .authenticationEntryPoint(((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().print("Invalid access token");
                        })));

        return httpSecurity.build();
    }

}



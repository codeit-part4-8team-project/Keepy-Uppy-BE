package com.keepyuppy.KeepyUppy.config;

import com.keepyuppy.KeepyUppy.security.jwt.JwtAuthenticationFilter;
import com.keepyuppy.KeepyUppy.security.jwt.JwtUtils;
import com.keepyuppy.KeepyUppy.security.oauth.CustomOAuth2UserService;
import com.keepyuppy.KeepyUppy.security.oauth.OAuth2FailureHandler;
import com.keepyuppy.KeepyUppy.security.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;


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
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))

                .authorizeHttpRequests((request -> request
                        .requestMatchers(new AntPathRequestMatcher("/test/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/signin/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/oauth2/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/signup")).permitAll()
                        // TODO: Roles?
                        // .requestMatchers(new AntPathRequestMatcher("/user")).hasRole("USER")
                        .requestMatchers("/", "/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
                        .anyRequest().authenticated()))

                .oauth2Login((oauth2Login) -> oauth2Login.userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler))

                // TODO: add all relevant filters
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils) , UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}


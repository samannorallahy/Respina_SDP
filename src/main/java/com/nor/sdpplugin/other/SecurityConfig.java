package com.nor.sdpplugin.other;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/secure-data").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic() // فعال‌سازی Basic Auth
                .and()
                .csrf().disable(); // برای راحتی تست، CSRF رو غیرفعال می‌کنیم

        return http.build();
    }
}

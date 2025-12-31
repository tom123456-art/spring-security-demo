package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApplication.class, args);
    }

    // 受保护的 API：需要认证才能访问
    @GetMapping("/api/hello")
    public String helloWorld() {
        return "Hello World";
    }

    @Configuration
    @EnableWebSecurity
    static class SecurityConfig {

        // 配置内存用户：用户名 test / 密码 123456（自动 BCrypt 加密）
        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
            UserDetails user = User.withDefaultPasswordEncoder()
                    .username("test")
                    .password("123456")  // 明文输入，内部自动加密
                    .roles("USER")
                    .build();
            return new InMemoryUserDetailsManager(user);
        }

        // 安全配置：所有请求需认证，使用 HTTP Basic 登录
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().authenticated() // 所有路径都需登录
                    )
                    .httpBasic(basic -> basic.realmName("Spring Security Demo")); // 启用 Basic 认证
            return http.build();
        }
    }
}

package cs.quizzapp.prokect.backend.config;

import cs.quizzapp.prokect.backend.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API testing
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/api/auth/login", "/api/auth/register").permitAll() // Public endpoints
                       // .requestMatchers("/api/quizzes/**").authenticated() // Protect quiz-related endpoints
                        .anyRequest().permitAll() // Allow all other endpoints
                );
                //.sessionManagement(session -> {
                   // SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer = session
                            //.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                   // return httpSecuritySessionManagementConfigurer;
                      //  } // Stateless session
              //  )
                //.httpBasic(httpBasicCustomizer -> {}); // Use basic authentication for simplicity

        return http.build();
    }
}

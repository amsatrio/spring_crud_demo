package io.github.amsatrio.spring_crud_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.github.amsatrio.spring_crud_demo.middleware.filter.AuthTokenFilter;
import io.github.amsatrio.spring_crud_demo.modules.auth.AuthenticationEntryPointImpl;
import io.github.amsatrio.spring_crud_demo.modules.auth.UserDetailsServiceImpl;
import io.github.amsatrio.spring_crud_demo.util.NoPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // set bcrypt encoder if all passwords in db are bcrypt encoder
        // return new BCryptPasswordEncoder();
        return new NoPasswordEncoder();
    }

    @Bean
    @Primary
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))

                // .csrf(csrf -> csrf
                // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                // .ignoringRequestMatchers(
                // "/api/auth/login",
                // "/api/auth/register",
                // "/api/auth/refresh_token", "/api/auth/public"))

                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                        .xssProtection(xss -> xss.disable())
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'")))
                .exceptionHandling(handling -> handling.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/refresh_token").permitAll()
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/email_verification").permitAll()
                        .requestMatchers("/api/auth/resend_verification").permitAll()
                        .requestMatchers("/api/auth/public").permitAll()
                        .requestMatchers("/graphiql/**").permitAll()
                        .requestMatchers("/v1/**").permitAll()

                        // Swagger V3 OpenAPI Endpoints
                        .requestMatchers("/doc/api-docs/**").permitAll()
                        .requestMatchers("/doc/swagger-ui/**").permitAll()
                        .requestMatchers("/doc/swagger-ui.html").permitAll()
                        .requestMatchers("/doc/swagger-resources/**").permitAll()

                        // Optional: OpenAPI specific configurations
                        .requestMatchers("/doc/api-docs/**").permitAll()
                        .requestMatchers("/doc/api-docs").permitAll()

                        // Optional: Actuator endpoints
                        .requestMatchers("/actuator/**").permitAll()

                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
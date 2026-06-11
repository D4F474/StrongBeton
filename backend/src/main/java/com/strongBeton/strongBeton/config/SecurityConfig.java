<<<<<<< HEAD
package com.strongBeton.strongBeton.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private final List<String> allowedOrigins;

    
    @Autowired
    public SecurityConfig(AuthenticationProvider authenticationProvider,
                          JwtAuthFilter jwtAuthFilter,
                          @Value("#{'${app.cors.allowed-origins:http://localhost:8081,http://localhost:4200,http://192.168.0.104:4200,http://0.0.0.0:4200}'.split(',')}") List<String> allowedOrigins) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthFilter = jwtAuthFilter;
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                            // public auth/register endpoints
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/users/register").permitAll()
                            .requestMatchers("/api/users/register").permitAll()

                            // public leaderboard
                            .requestMatchers("/api/leaderBoard").permitAll()

                            // protected
                            .requestMatchers("/api/**").authenticated()
                            .requestMatchers("/users/**").authenticated()
                            .requestMatchers("/upload").authenticated()

                            .anyRequest().authenticated()
                    )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE"));

        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}
=======
package com.strongBeton.strongBeton.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private final List<String> allowedOrigins;

    
    @Autowired
    public SecurityConfig(AuthenticationProvider authenticationProvider,
                          JwtAuthFilter jwtAuthFilter,
                          @Value("#{'${app.cors.allowed-origins:http://localhost:8081,http://localhost:4200,http://192.168.0.104:4200,http://0.0.0.0:4200}'.split(',')}") List<String> allowedOrigins) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthFilter = jwtAuthFilter;
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                            // public auth/register endpoints
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/users/register").permitAll()
                            .requestMatchers("/api/users/register").permitAll()

                            // public leaderboard
                            .requestMatchers("/api/leaderBoard").permitAll()

                            // protected
                            .requestMatchers("/api/**").authenticated()
                            .requestMatchers("/users/**").authenticated()
                            .requestMatchers("/upload").authenticated()

                            .anyRequest().authenticated()
                    )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE"));

        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}
>>>>>>> fda96bb (Add Dockerized backend and MySQL setup)

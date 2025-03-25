package com.Spring.Security;


import java.util.List;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.Spring.Service.UserService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
		@Autowired
		private JwtFilter jwtFilter;
		
		@Autowired
	@Lazy
		private UserService authService;
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
		    http
		        .cors(cors -> cors.configurationSource(request -> {
		            CorsConfiguration config = new CorsConfiguration();
		            config.setAllowedOrigins(List.of("http://localhost:5173")); // React frontend
		            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
		            config.setAllowedHeaders(List.of("*"));
		            config.setAllowCredentials(true);
		            return config;
		        }))
		        .csrf(csrf -> csrf.disable())
		        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
		        .authorizeHttpRequests(auth -> auth
		            .requestMatchers("/login/register", "/login/log", "/login/verify","/auth/google", "/auth/user", "/comments/**","/categories/**").permitAll()
		            .requestMatchers(HttpMethod.GET, "/blogs/**","/comments/**").permitAll()
	          .requestMatchers(HttpMethod.POST, "/blogs/**","/comments/**","/categories/**").authenticated()
		            .requestMatchers(HttpMethod.DELETE, "/blogs/**").authenticated()
		            .requestMatchers(HttpMethod.PUT, "/blogs/**").authenticated()
		            .anyRequest().authenticated())
		        
		        .logout(logout -> logout
		                    .logoutUrl("/logout") 
		                    .logoutSuccessUrl("/") 
		                    .invalidateHttpSession(true)
		                    .deleteCookies("JSESSIONID")
		                )

		    
		        .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		    return http.build();
		}

		 @Bean
		    public AuthenticationManager authenticationManager() {
		        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		        authProvider.setUserDetailsService(authService);
		        authProvider.setPasswordEncoder(passwordEncoder());
		        return new ProviderManager(List.of(authProvider));
		    }

		    @Bean
		    public PasswordEncoder passwordEncoder() {
		        return new BCryptPasswordEncoder();
		    }
		    
		    
		    @Bean
		    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
		        return userRequest -> {
		            OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
		            
		            // Log all attributes to debug
		            System.out.println("OAuth2 User Attributes: " + oAuth2User.getAttributes());
		            
		            return oAuth2User;
		        };
		    }
		    
}



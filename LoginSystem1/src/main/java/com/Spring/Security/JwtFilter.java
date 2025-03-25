package com.Spring.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import jakarta.servlet.FilterChain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Spring.Service.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil  jwtUtil;
	
	 @Autowired
	 @Lazy
	    private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
	        String header = request.getHeader("Authorization");

	        if (header != null && header.startsWith("Bearer ")) {
	            String token = header.substring(7);
	            String username = jwtUtil.extractUsername(token);

	            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
	                    UsernamePasswordAuthenticationToken authentication =
	                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                    SecurityContextHolder.getContext().setAuthentication(authentication);
	                }
	            }
	        }
	    } catch (Exception e) {
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + e.getMessage());
	        return; // Stop execution to prevent further processing
	    }

	    filterChain.doFilter(request, response);
	}
}


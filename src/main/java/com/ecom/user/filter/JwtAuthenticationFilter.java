package com.ecom.user.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecom.user.service.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		return path != null && (path.startsWith("/auth/") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs"));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String jwt = authHeader.substring(7).trim();
		String originalThreadName = Thread.currentThread().getName();

		try {
			Claims claims = jwtService.extractAllClaims(jwt);
			if (claims != null && !jwtService.isTokenExpired(jwt)) {
				String username = claims.getSubject();

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = null;
					try {
						userDetails = userDetailsService.loadUserByUsername(username);
					} catch (Exception ignored) {
						log.debug("User {} not found in local DB, checking token claims", username);
					}

					Collection<SimpleGrantedAuthority> authorities;
					if (userDetails != null) {
						@SuppressWarnings("unchecked")
						Collection<SimpleGrantedAuthority> auths = (Collection<SimpleGrantedAuthority>) userDetails.getAuthorities();
						authorities = auths;
					} else {
						Object roleClaim = claims.get("role");
						if (roleClaim instanceof List<?> roleList) {
							authorities = roleList.stream()
									.map(r -> new SimpleGrantedAuthority(r.toString()))
									.toList();
						} else if (roleClaim instanceof String roleStr) {
							authorities = List.of(new SimpleGrantedAuthority(roleStr));
						} else {
							authorities = List.of();
						}
					}

					Object principal = (userDetails != null) ? userDetails : username;
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							principal, null, authorities
					);
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					Thread.currentThread().setName(username);
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		} catch (Exception e) {
			log.error("JWT validation failed: {}", e.getMessage());
			SecurityContextHolder.clearContext();
		}

		try {
			filterChain.doFilter(request, response);
		} finally {
			Thread.currentThread().setName(originalThreadName);
		}
	}
}

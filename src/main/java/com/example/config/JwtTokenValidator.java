package com.example.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = request.getHeader(JwtConstant.JWT_HEADER_STRING);
		 
		if(jwt != null) {
			jwt = jwt.substring(7);
			
			try {
				SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY_STRING.getBytes());
				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				String email = String.valueOf(claims.get("email"));
				String authorities = String.valueOf(claims.get("authorities"));
				
				List<GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList("authoroties");
				
				Authentication auth = new UsernamePasswordAuthenticationToken(
						email,
						null,
						authoritiesList
				);
						
				SecurityContextHolder.getContext().setAuthentication(auth);	
			} catch (Exception e) {
				throw new RuntimeException("Invalid Token...");
			}
		}
		filterChain.doFilter(request, response);
	}
	
}

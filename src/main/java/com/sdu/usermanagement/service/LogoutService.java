package com.sdu.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sdu.usermanagement.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		
		if (authHeader == null || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            return;
        }
        jwtToken = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwtToken).orElse(null);
        if(storedToken != null) {
            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            tokenRepository.saveAndFlush(storedToken);
        }
    }
    
}

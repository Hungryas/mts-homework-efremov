package org.example.controllers.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entities.User;
import org.hibernate.ObjectNotFoundException;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class InitialAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;

    private final UsernamePasswordAuthenticationProvider authenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws IOException {
        if (request.getHeader("Authorization") == null) {
            String bodyJson = request.getReader().readLine();
            if (bodyJson != null) {
                ObjectMapper mapper = new ObjectMapper();
                User userDto = mapper.readValue(bodyJson, User.class);
                String username = userDto.getEmail();
                String password = userDto.getPassword();

                try {
                    Authentication authentication = new UsernamePasswordAuthentication(username, password);
                    authentication = authenticationProvider.authenticate(authentication);
                    String jwt = jwtService.generatedJwt(authentication);
                    response.setHeader(AUTHORIZATION, HeaderValues.BEARER + jwt);
                } catch (BadCredentialsException | ObjectNotFoundException e) {
                    logger.error(e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}

package io.github.amsatrio.spring_crud_demo.middleware.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.amsatrio.spring_crud_demo.modules.auth.UserDetailsServiceImpl;
import io.github.amsatrio.spring_crud_demo.util.JwtUtil;


@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @SuppressWarnings("null")    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        log.info("doFilterInternal");
        try {

            String jwt = null;

            log.info("request uri: " + request.getRequestURI());

            String headerAuth = request.getHeader("Authorization");
            if (!StringUtils.hasText(headerAuth)) {
                log.error("header authorization is empty");
                // response.setStatus(401);
                filterChain.doFilter(request, response);
                return;
            }
            if (!headerAuth.startsWith("Bearer ")) {
                log.error("header authorization format is invalid");
                response.setStatus(401);
                filterChain.doFilter(request, response);
                return;
            }
            jwt = headerAuth.substring(7);

            if (!jwtUtils.validateToken(jwt)) {
                log.error("token is invalid");
                response.setStatus(401);
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtUtils.isExpired()) {
                log.error("token is expired");
                response.setStatus(401);
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtUtils.getTokenType().equals("MAIN_TOKEN")) {
                log.error("token is not main token");
                response.setStatus(401);
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtUtils.getUsername();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (UsernameNotFoundException exception) {
            log.error("user not found:", exception);
            response.setStatus(401);
        }

        filterChain.doFilter(request, response);
    }
}
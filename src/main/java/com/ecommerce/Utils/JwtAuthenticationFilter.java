package com.ecommerce.Utils;

import com.ecommerce.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    // Define public routes that don't require authentication
    private static final List<String> PUBLIC_ROUTES = Arrays.asList(
            "/api/users/signup",
            "/api/users/login",
            "/api/cloudinary/upload",
            "/api/products/allproducts",
            "/api/products",
            "/api/categories/getAllCategories"
    );

    private boolean isPublicUrl(String requestURI) {
        // Check if the URL is in the predefined public routes
        if (PUBLIC_ROUTES.contains(requestURI)) {
            return true;
        }

        // Allow dynamic product routes like /api/products/{id}
        return requestURI.matches("^/api/products/\\d+$");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, InvalidTokenException {
        String requestURI = request.getServletPath();
        // Skip JWT validation for public routes
        if (isPublicUrl(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        try{
            String token = JwtUtils.getTokenFromCookie(request);
            // is token exist
            if (token != null) {
                String username = JwtUtils.getUsernameFromToken(token);
                String role = JwtUtils.getRoleFromToken(token);
                System.out.println("Role: " + role);
                // check the username & role must contain the token
                if (username != null && role != null) {
                    // check the extracted user from token exist in our DB or not
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    // validate the token, its expired or not, if not do further process
                    if (JwtUtils.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                username, null, Collections.singletonList(new SimpleGrantedAuthority(role))
                        );
//                        System.out.println("authentication: " + authentication);
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
            else{
                throw new InvalidTokenException("Invalid token");
            }
            // Check if the user is authenticated
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                throw new InvalidTokenException("Wrong Credentials");
            }
        }
        catch (InvalidTokenException e){
            handleException(request,response, e);
            return; // Stop further processing
        }
        // Continue with the filter chain if authenticated
        filterChain.doFilter(request, response);
    }

    // Custom exception handler for sending JSON response
    private void handleException(HttpServletRequest request, HttpServletResponse response, InvalidTokenException e) throws IOException {
        // Get the current timestamp in a readable format
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Get the request URI (path)
        String requestPath = request.getRequestURI();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Construct JSON response with timestamp, path, and exception message
        String jsonResponse = String.format(
                "{\"success\": false, \"error\": \"%s\", \"message\": \"Unauthorized access. Please log in.\", \"timestamp\": \"%s\", \"path\": \"%s\"}",
                e.getMessage(), timestamp, requestPath
        );

        response.getWriter().write(jsonResponse);
    }
}
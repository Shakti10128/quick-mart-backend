package com.ecommerce.Utils;

import com.ecommerce.enums.UserTypes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;


public class JwtUtils {
    private static final String SECRET_KEY = "myverysecureandlongsecretkeywhichis32byteslong!";

    public static String generateTokenAndSetCookie(HttpServletRequest request, HttpServletResponse response, String username, UserTypes role) {
        // 1 week in milliseconds
        int EXPIRATION_TIME = 7 * 24 * 60 * 60; // Cookie max-age in seconds

        // 🔍 Check if a token cookie already exists and remove it
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("ecom-token".equals(cookie.getName())) {
                    cookie.setMaxAge(0);  // Remove the existing token
                    cookie.setValue("");  // Clear value
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        // 🔐 Generate a new JWT token
        String token = Jwts.builder()
                .subject(username)
                .claim("role", "ROLE_" + role)  // ✅ Add role to token payload
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (EXPIRATION_TIME * 1000)))
                .signWith(getSecretKey())
                .compact();

        // 🍪 Set a new secure HttpOnly cookie
        Cookie newCookie = new Cookie("ecom-token", token);
        newCookie.setHttpOnly(true); // Prevent JavaScript access (XSS protection)
        newCookie.setSecure(false);  // Set `true` if using HTTPS
        newCookie.setPath("/");      // Available for the entire domain
        newCookie.setMaxAge(EXPIRATION_TIME);

        response.addCookie(newCookie);
        return token;
    }


    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Extract Token from Cookies
    public static String getTokenFromCookie(HttpServletRequest request) {
        String token = null;
        if(request.getCookies() != null) {
            for(Cookie cookie: request.getCookies()) {
                if(cookie.getName().equals("ecom-token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    public static String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);  // ✅ Extract role from token
    }

    private static Claims getClaimsFromToken(String token) {
        try{
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        catch(JwtException e){
            throw new JwtException("Invalid JWT token");
        }
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = getClaimsFromToken(token);
        String username = claims.getSubject();
        String role = claims.get("role", String.class);

        // Check if the token is expired and if the username matches
        return (username.equals(userDetails.getUsername()) && role != null && !claims.getExpiration().before(new Date()));
    }


    public static String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public static boolean removeTokenFromCookie(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ecom-token")) {
                    // Set the cookie max age to 0 to expire it
                    cookie.setMaxAge(0);
                    cookie.setValue("");  // Clear the cookie's value

                    // Add the updated cookie to the response to remove it from the client
                    cookie.setPath("/");  // Ensure the path matches the original cookie
                    response.addCookie(cookie);
                    return true;
                }
            }
        }
        return false;
    }


}

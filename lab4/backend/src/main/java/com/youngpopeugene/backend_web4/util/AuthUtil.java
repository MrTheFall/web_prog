package com.example.AreaChecker.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.AreaChecker.controller.GlobalBean;
import jakarta.ejb.EJB;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthUtil {
    @EJB
    private static GlobalBean globalBean;

    private static final String SECRET_KEY = System.getenv("JWT_SECRET_KEY");

    public static boolean parseAuth(HttpServletRequest request) {
        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            throw new IllegalStateException("Environment variable JWT_SECRET_KEY not set");
        }

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    String jwtToken = cookie.getValue();
                    return validateJwt(jwtToken);
                }
            }
        }
        return false;
    }

    private static boolean validateJwt(String jwtToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); // Note: Ensure the secret key is at least 256 bits
            JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier
            DecodedJWT decodedJWT = verifier.verify(jwtToken); // Verify the token

            String login = decodedJWT.getSubject();
            if (globalBean.isUsernameExists(login)) {
                return true;
            }
            System.out.println("User not found: " + login);
        } catch (com.auth0.jwt.exceptions.JWTVerificationException e) {
            System.out.println("JWT verification error: " + e.getMessage());
        }
        return false;
    }

    public static String extractUsernameFromJwt(String jwtToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(jwtToken);
            return decodedJWT.getSubject(); // Assuming the username is stored in the 'sub' claim
        } catch (com.auth0.jwt.exceptions.JWTVerificationException e) {
            System.out.println("JWT verification error: " + e.getMessage());
            return null;
        }
    }

    public static String createJwtToken(String username) {
        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            throw new IllegalStateException("Environment variable JWT_SECRET_KEY not set");
        }

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withSubject(username)
                .sign(algorithm);
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


}

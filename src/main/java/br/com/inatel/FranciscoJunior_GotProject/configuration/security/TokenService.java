package br.com.inatel.FranciscoJunior_GotProject.configuration.security;

import br.com.inatel.FranciscoJunior_GotProject.configuration.security.model.RegistryUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {
    @Value("${jwt.token}")
    private String secretKey;

    @Deprecated
    public String generateToken(Authentication a, String authority) {
        Optional<String> key = Arrays.stream(secretKey.split(";"))//
                .filter(k -> k.split("[ ]")[0].equals(authority))//
                .map(k -> k.split("[ ]")[1])//
                .findFirst();
        if (key.isPresent())
            /*
             * String key = ""; switch (authority) { case "Client": key = secretKey; break;
             * default: break; }
             */
            return tokenFactory(a, key.get());
        return null;
    }


    public String generateToken(Authentication a) {
        return tokenFactory(a, secretKey);
    }


    private String tokenFactory(Authentication auth, String key) {
        Date now = new Date();
        return Jwts.builder().setIssuer("Spring Boot Security Model")//
                .setSubject(((RegistryUser) auth.getPrincipal()).getId().toString())//
                .setIssuedAt(now)//
                .setExpiration(new Date(now.getTime() + (86400000l)))//
                .signWith(SignatureAlgorithm.HS256, key)//
                .compact();
    }


    public boolean isClientValid(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public Long getClientUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

}


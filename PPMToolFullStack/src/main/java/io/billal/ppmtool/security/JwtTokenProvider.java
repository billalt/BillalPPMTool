package io.billal.ppmtool.security;

import io.billal.ppmtool.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.billal.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static io.billal.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    //Generate the token
    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis()) ;
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String , Object> claims = new HashMap<>();
        claims.put("id" , (Long.toString(user.getId())));
        claims.put("username" , user.getUsername());
        claims.put("fullName" , user.getFullName());
        //claims.put("password" , user.getPassword());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512 ,SECRET)
                .compact();
    }

    //Validate the token
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            System.out.println("Invalid JWT Signature");
        }catch (MalformedJwtException e){
            System.out.println("Invalid JWT Token");
        }catch (ExpiredJwtException e){
            System.out.println("Expired JWT Token");
        }catch (UnsupportedJwtException e){
            System.out.println("Unsupported Jwt Token");
        }catch (IllegalArgumentException e){
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    //Get use Id from token
    public Long getUserIdFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id  = (String)claims.get("id");
        return Long.parseLong(id);
    }
}

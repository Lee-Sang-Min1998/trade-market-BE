package TRADE_MARKET.trademarket.global.auth.service;


import TRADE_MARKET.trademarket.global.auth.domain.JwtProperties;
import TRADE_MARKET.trademarket.global.auth.service.CustomUserDetailsService;
import TRADE_MARKET.trademarket.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {


    private final JwtProperties jwtProperties;

    private final CustomUserDetailsService customUserDetailsService;

    public String generateToken(UserDetails userDetails) {

        Date date = new Date();

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(jwtProperties.getIssuer())
            .setIssuedAt(date)
            .setExpiration(new Date(date.getTime() + Duration.ofMinutes(
                jwtProperties.getExpiredMinute()).toMillis()))
            .claim("id", userDetails.getUsername())
            .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
            .compact();
    }


    public Authentication getAuthentication(String token) {

        validateTokenHeader(token);
        String headerDeleteToken = deleteHeaderFromToken(token);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(
            (String) parseToken(headerDeleteToken).get("id"));

        return new UsernamePasswordAuthenticationToken(userDetails, "", null);


    }

    private Claims parseToken(String token) {

        return Jwts.parser()
            .setSigningKey(jwtProperties.getSecretKey())
            .parseClaimsJws(token)
            .getBody();

    }

    private void validateTokenHeader(String header) {
        if (header == null || !header.startsWith(jwtProperties.getPrefix())) {
            throw new UnsupportedJwtException("Invalid header");
        }
    }

    private String deleteHeaderFromToken(String token) {
        return token.substring(jwtProperties.getPrefix().length());
    }

}

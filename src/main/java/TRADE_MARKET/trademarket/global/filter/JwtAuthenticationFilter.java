package TRADE_MARKET.trademarket.global.filter;

import TRADE_MARKET.trademarket.global.auth.service.JwtTokenProvider;
import TRADE_MARKET.trademarket.global.dto.ApiErrorResponse;
import TRADE_MARKET.trademarket.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            jwtTokenProvider.getAuthentication(authorizationHeader);
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            setErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } catch (SignatureException e) {
            setErrorResponse(response, ErrorCode.INVALID_SIGNATURE);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) {

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(objectMapper.writeValueAsString(
                ApiErrorResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage()).build()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

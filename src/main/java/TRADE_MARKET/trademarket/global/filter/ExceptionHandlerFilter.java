package TRADE_MARKET.trademarket.global.filter;

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
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
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

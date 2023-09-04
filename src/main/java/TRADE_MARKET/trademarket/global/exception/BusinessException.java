package TRADE_MARKET.trademarket.global.exception;

import TRADE_MARKET.trademarket.global.dto.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
@RequiredArgsConstructor
public class BusinessException {


    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ApiErrorResponse> handleWebclientResponseException(
        WebClientResponseException e) {

        return ResponseEntity
            .status(e.getStatusCode())
            .body(ApiErrorResponse.builder()
                .code(e.getStatusCode().value())
                .message(e.getStatusText())
                .build());
    }

}

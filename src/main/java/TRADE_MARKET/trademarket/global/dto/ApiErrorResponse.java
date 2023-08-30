package TRADE_MARKET.trademarket.global.dto;

import TRADE_MARKET.trademarket.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ApiErrorResponse {

    private int code;
    private String message;

    public static ResponseEntity<ApiErrorResponse> toResponseEntity(ErrorCode e) {

        return ResponseEntity
            .status(e.getHttpStatus())
            .body(ApiErrorResponse
                .builder()
                .code(e.getCode())
                .message(e.getMessage())
                .build());
    }
}

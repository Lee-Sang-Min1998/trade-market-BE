package TRADE_MARKET.trademarket.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 데이터입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, 400, "잘못된 형식의 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 401, "만료된 토큰입니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, 401, "잘못된 JWT 서명입니다."),
    EXTERNAL_API_ERROR(HttpStatus.SERVICE_UNAVAILABLE, 503, "외부 API 호출을 실패하였습니다.");

    HttpStatus httpStatus;
    private int code;
    private String message;

}

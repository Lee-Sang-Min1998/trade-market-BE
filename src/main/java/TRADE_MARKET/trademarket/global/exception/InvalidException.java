package TRADE_MARKET.trademarket.global.exception;

import lombok.Getter;

@Getter
public class InvalidException extends CustomException {

    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }

}

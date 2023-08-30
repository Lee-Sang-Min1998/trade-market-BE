package TRADE_MARKET.trademarket.global.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends CustomException {

    public DataNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

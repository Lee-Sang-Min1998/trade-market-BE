package TRADE_MARKET.trademarket.global.auth.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource("classpath:/application-dev.properties")
public class JwtProperties {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.prefix}")
    private String prefix;

    @Value("${jwt.expiredMinute}")
    private int expiredMinute;
}

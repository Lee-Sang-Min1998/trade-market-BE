package TRADE_MARKET.trademarket.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfoDto {

    @JsonProperty("id")
    private Long authId;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

}

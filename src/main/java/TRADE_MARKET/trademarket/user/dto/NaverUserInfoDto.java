package TRADE_MARKET.trademarket.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NaverUserInfoDto {

    @JsonProperty("resultcode")
    private String resultCode;

    @JsonProperty("message")
    private String message;


    @JsonProperty("response")
    private NaverResponse naverResponse;


}

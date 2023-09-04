package TRADE_MARKET.trademarket.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverResponse {

    @JsonProperty("id")
    private String authId;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;

}

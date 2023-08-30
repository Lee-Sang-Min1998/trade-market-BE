package TRADE_MARKET.trademarket.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaverUserInfoDto {

    @JsonProperty("response/id")
    private String id;

    @JsonProperty("response/nickname")
    private String nickname;

    @JsonProperty("response/profile_image")
    private String profileImage;
}

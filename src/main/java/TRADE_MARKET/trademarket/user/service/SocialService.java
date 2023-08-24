package TRADE_MARKET.trademarket.user.service;


import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SocialService {


    public String getAccessTokenFromKakaoServer(String authorizationCode) {

        WebClient webclient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com/oauth/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        String response = webclient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", "null")
                        .queryParam("code", authorizationCode)
                        .queryParam("redirect_uri", "null")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ;
    }

    public void getUserInfoFromKakaoServer(String accessToken) {

    }
}

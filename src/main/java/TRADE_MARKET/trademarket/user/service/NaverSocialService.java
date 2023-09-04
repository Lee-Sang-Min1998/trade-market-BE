package TRADE_MARKET.trademarket.user.service;

import TRADE_MARKET.trademarket.global.exception.DataNotFoundException;
import TRADE_MARKET.trademarket.global.exception.ErrorCode;
import TRADE_MARKET.trademarket.user.dto.NaverAccessTokenDto;
import TRADE_MARKET.trademarket.user.dto.NaverUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/application-dev.properties")
public class NaverSocialService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;
    private final String contentType = "application/x-www-form-urlencoded;charset=utf-8";

    public String getAccessTokenFromNaverServer(String authorizationCode, String state) {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("code", authorizationCode);
        requestBody.add("state", state);

        WebClient webclient = WebClient.builder()
            .baseUrl("https://nid.naver.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, contentType)
            .build();

        NaverAccessTokenDto responseToken = webclient.post()
            .uri(uriBuilder -> uriBuilder
                .path("/oauth2.0/token")
                .queryParams(requestBody)
                .build())
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(NaverAccessTokenDto.class);
                } else {
                    throw new WebClientResponseException(500, "NAVER_INTERNAL_SERVER_ERROR", null,
                        null, null);
                }
            }).block();

        if (responseToken == null) {
            throw new DataNotFoundException(ErrorCode.NOT_FOUND);
        } else {
            return responseToken.getAccessToken();
        }
    }

    @Transactional
    public void getUserInfoFromNaverServer(String accessToken) {

        WebClient webclient = WebClient.builder()
            .baseUrl("https://openapi.naver.com/v1/nid/me")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .build();

        NaverUserInfoDto response = webclient.post()
            .retrieve()
            .bodyToMono(NaverUserInfoDto.class)
            .block();


    }
}

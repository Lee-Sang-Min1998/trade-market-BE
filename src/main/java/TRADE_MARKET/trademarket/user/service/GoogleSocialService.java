package TRADE_MARKET.trademarket.user.service;

import TRADE_MARKET.trademarket.global.exception.DataNotFoundException;
import TRADE_MARKET.trademarket.global.exception.ErrorCode;
import TRADE_MARKET.trademarket.user.dto.GoogleAccessTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
@PropertySource("classpath:/application-dev.properties")
public class GoogleSocialService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.url}")
    private String redirectUrl;

    public String getAccessTokenFromKakaoServer(String authorizationCode) {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("code", authorizationCode);
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("redirect_uri", redirectUrl);

        WebClient webclient = WebClient.builder()
            .baseUrl("https://oauth2.googleapis.com")
            .build();

        GoogleAccessTokenDto responseToken = webclient.post()
            .uri(uriBuilder -> uriBuilder
                .path("/token")
                .queryParams(requestBody)
                .build())
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(GoogleAccessTokenDto.class);
                } else {
                    throw new WebClientResponseException(500, "GOOGLE_INTERNAL_SERVER_ERROR", null,
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
    public void getUserInfoFromGoogleServer(String accessToken) {

        WebClient webclient = WebClient.builder()
            .baseUrl("https://www.googleapis.com/oauth2/v1/userinfo")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .build();

//        webclient.get()
//                .retrieve()
//                .bodyToMono()
//                .block();

    }
}

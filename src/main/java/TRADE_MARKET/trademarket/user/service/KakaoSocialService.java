package TRADE_MARKET.trademarket.user.service;


import TRADE_MARKET.trademarket.global.auth.domain.CustomUserDetails;
import TRADE_MARKET.trademarket.global.exception.DataNotFoundException;
import TRADE_MARKET.trademarket.global.exception.ErrorCode;
import TRADE_MARKET.trademarket.user.domain.AuthType;
import TRADE_MARKET.trademarket.user.domain.User;
import TRADE_MARKET.trademarket.user.dto.KakaoAccessTokenDto;
import TRADE_MARKET.trademarket.user.dto.KakaoUserInfoDto;
import TRADE_MARKET.trademarket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.reactive.function.client.WebClientResponseException;


@Service
@RequiredArgsConstructor
@PropertySource("classpath:/application-dev.properties")
public class KakaoSocialService {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.url}")
    private String redirectUrl;

    @Value("${kakao.client.secret}")
    private String clientSecret;


    private final String contentType = "application/x-www-form-urlencoded;charset=utf-8";

    private final UserRepository userRepository;

    private final UserService userService;

    public String getAccessTokenFromKakaoServer(String authorizationCode) {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", redirectUrl);
        requestBody.add("code", authorizationCode);
        requestBody.add("client_secret", clientSecret);

        WebClient webclient = WebClient.builder()
            .baseUrl("https://kauth.kakao.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, contentType)
            .build();

        KakaoAccessTokenDto responseToken = webclient.post()
            .uri(uriBuilder -> uriBuilder
                .path("/oauth/token")
                .queryParams(requestBody)
                .build())
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(KakaoAccessTokenDto.class);
                } else {
                    throw new WebClientResponseException(500, "KAKAO_INTERNAL_SERVER_ERROR", null,
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
    public CustomUserDetails getUserInfoFromKakaoServer(String accessToken) {

        WebClient webclient = WebClient.builder()
            .baseUrl("https://kapi.kakao.com/v2/user/me")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, contentType)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .build();

        KakaoUserInfoDto userInfo = webclient.post()
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(KakaoUserInfoDto.class);
                } else {
                    throw new WebClientResponseException(500, "KAKAO_INTERNAL_SERVER_ERROR", null,
                        null, null);
                }
            }).block();

        if (userInfo == null) {
            throw new DataNotFoundException(ErrorCode.NOT_FOUND);
        }

        try {
            User findUser = userRepository.findByAuthIdAndAuthType(String.valueOf(userInfo.getAuthId()),
                    AuthType.KAKAO)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND));
            return CustomUserDetails.createUserDetails(findUser);

        } catch (DataNotFoundException e) {
            //회원 가입 후 UserDetails 반환
            return CustomUserDetails.createUserDetails(
                userService.register(String.valueOf(userInfo.getAuthId()),
                    userInfo.getKakaoAccount().getProfile().getNickname(),
                    userInfo.getKakaoAccount().getProfile().getProfileImage(),
                    AuthType.KAKAO));
        }

    }
}



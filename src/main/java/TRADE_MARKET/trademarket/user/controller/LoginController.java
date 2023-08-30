package TRADE_MARKET.trademarket.user.controller;

import TRADE_MARKET.trademarket.global.auth.domain.CustomUserDetails;
import TRADE_MARKET.trademarket.global.auth.service.JwtTokenProvider;
import TRADE_MARKET.trademarket.global.auth.service.AuthService;
import TRADE_MARKET.trademarket.global.dto.ApiResponse;
import TRADE_MARKET.trademarket.user.service.KakaoSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final KakaoSocialService kakaoSocialService;

    private final AuthService authService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login/kakao")
    public ResponseEntity<ApiResponse<Void>> kakaoLogin(
        @RequestParam("authorizationCode") String authorizationCode) {

        CustomUserDetails userDetails = kakaoSocialService.getUserInfoFromKakaoServer(
            kakaoSocialService.getAccessTokenFromKakaoServer(authorizationCode));
        authService.setSecurityContext(authService.getAuthentication(userDetails));

        String accessToken = jwtTokenProvider.generateToken(userDetails);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        ApiResponse<Void> response = new ApiResponse<>(200, "로그인 성공");

        return ResponseEntity.ok()
            .headers(httpHeaders)
            .body(response);


    }

}

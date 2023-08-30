package TRADE_MARKET.trademarket.user.service;

import TRADE_MARKET.trademarket.global.auth.domain.CustomUserDetails;
import TRADE_MARKET.trademarket.global.exception.DataNotFoundException;
import TRADE_MARKET.trademarket.global.exception.ErrorCode;
import TRADE_MARKET.trademarket.user.domain.AuthType;
import TRADE_MARKET.trademarket.user.domain.User;
import TRADE_MARKET.trademarket.user.domain.UserGradeType;
import TRADE_MARKET.trademarket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User register(Long authId, String nickname, String profileImage,
        AuthType authType) {

        userRepository.save(
            User.builder()
                .authId(authId)
                .nickname(nickname)
                .profileImage(profileImage)
                .authType(authType)
                .grade(UserGradeType.ONE)
                .build()
        );

        return userRepository.findByAuthIdAndAuthType(authId, authType)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND));

    }

}

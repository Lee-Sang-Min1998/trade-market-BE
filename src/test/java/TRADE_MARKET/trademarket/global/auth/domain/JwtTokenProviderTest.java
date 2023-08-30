package TRADE_MARKET.trademarket.global.auth.domain;


import TRADE_MARKET.trademarket.global.auth.service.JwtTokenProvider;
import TRADE_MARKET.trademarket.user.domain.AuthType;
import TRADE_MARKET.trademarket.user.domain.User;
import TRADE_MARKET.trademarket.user.domain.UserGradeType;
import TRADE_MARKET.trademarket.user.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@Slf4j
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Test
    public void testJwtTokenProvider() {

        User user = User.builder()
            .id(1L)
            .nickname("leesangmin")
            .grade(UserGradeType.ONE)
            .profileImage("url")
            .build();

        String token = jwtTokenProvider.generateToken(CustomUserDetails.createUserDetails(user));
        token = "Bearer " + token;

        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        Assertions.assertEquals(userId, 1L);

    }

    @Test
    public void saveUser() {

        User user = User.builder()
            .authId(1L)
            .authType(AuthType.KAKAO)
            .nickname("leesangmin")
            .grade(UserGradeType.ONE)
            .profileImage("url")
            .build();

        User user2 = User.builder()
            .authId(2L)
            .authType(AuthType.KAKAO)
            .nickname("leesangmin")
            .grade(UserGradeType.ONE)
            .profileImage("url")
            .build();

        userRepository.save(user);
        userRepository.save(user2);

        log.info("user auth ID = {}", user.getAuthId());
        log.info("user PK = {}", user.getId());

        log.info("user auth2 ID = {}", user2.getAuthId());
        log.info("user PK2 = {}", user2.getId());

        Optional<User> findUser = userRepository.findByAuthIdAndAuthType(1L,
            AuthType.KAKAO);

        log.info("find user Info={}", findUser.get().getId());
        log.info("find user Info={}", findUser.get().getNickname());

    }

}
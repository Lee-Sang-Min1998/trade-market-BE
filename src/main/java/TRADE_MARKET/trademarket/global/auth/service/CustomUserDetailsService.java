package TRADE_MARKET.trademarket.global.auth.service;

import TRADE_MARKET.trademarket.global.auth.domain.CustomUserDetails;
import TRADE_MARKET.trademarket.global.exception.DataNotFoundException;
import TRADE_MARKET.trademarket.global.exception.ErrorCode;
import TRADE_MARKET.trademarket.user.domain.AuthType;
import TRADE_MARKET.trademarket.user.domain.User;
import TRADE_MARKET.trademarket.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        User findUser = userRepository.findById(Long.parseLong(id))
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND));

        return CustomUserDetails.builder()
            .id(findUser.getId())
            .authId(findUser.getAuthId())
            .authType(findUser.getAuthType())
            .build();
    }

    public UserDetails loadUserByAuthIdAndAuthType(Long authId, AuthType authType) {

        User findUser = userRepository.findByAuthIdAndAuthType(authId, authType)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND));

        return CustomUserDetails.builder()
            .id(findUser.getId())
            .authId(findUser.getAuthId())
            .authType(findUser.getAuthType())
            .build();
    }

}

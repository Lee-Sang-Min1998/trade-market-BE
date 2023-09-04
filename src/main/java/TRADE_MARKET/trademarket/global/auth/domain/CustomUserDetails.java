package TRADE_MARKET.trademarket.global.auth.domain;

import TRADE_MARKET.trademarket.user.domain.AuthType;
import TRADE_MARKET.trademarket.user.domain.User;
import TRADE_MARKET.trademarket.user.domain.UserGradeType;
import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
public class CustomUserDetails implements UserDetails {

    private Long id;


    private String authId;


    private AuthType authType;

    //TODO 1. 권한 관련 코드 추가,
    //     2. CustomUserDetails을 OAuth2User 구현체로 변경

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return Long.toString(this.id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static CustomUserDetails createUserDetails(User user) {
        return CustomUserDetails.builder()
            .id(user.getId())
            .authId(user.getAuthId())
            .authType(user.getAuthType())
            .build();
    }
}

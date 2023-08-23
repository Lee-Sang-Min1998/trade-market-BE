package TRADE_MARKET.trademarket.user.domain;

import TRADE_MARKET.trademarket.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER")
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NICKNAME", length = 20)
    private String nickname;

    @Column(name = "GRADE")
    @Enumerated(EnumType.STRING)
    private UserGradeType grade;

    @Column(name = "PROFILE_IMAGE")
    private String profileImage;

}

package kma.cnwat.be.domain.user.entity;

import jakarta.persistence.*;
import kma.cnwat.be.domain.common.enumType.TokenType;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_active_reset_token")
public class ActiveResetToken extends AbstractEntity<Long>{
    @Column(name = "sub")
    String sub;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    TokenType tokenType;

//    @Column(name = "jwt_id")
//    String jwtId;
}

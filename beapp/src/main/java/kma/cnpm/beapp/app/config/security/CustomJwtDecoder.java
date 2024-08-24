package kma.cnwat.be.app.config.security;


import com.nimbusds.jose.JOSEException;
import kma.cnwat.be.domain.common.enumType.TokenType;
import kma.cnwat.be.domain.common.exception.AppErrorCode;
import kma.cnwat.be.domain.common.exception.AppException;
import kma.cnwat.be.domain.user.dto.resquest.IntrospectRequest;
import kma.cnwat.be.domain.user.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;
@Slf4j
@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.accessKey}")
    private String signerKey;

    @Autowired
    private AuthService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

//    @Override
//    public Jwt decode(String token) throws JwtException {
//
//        try {
//            var response = authenticationService.introspect(token , TokenType.ACCESS_TOKEN);
//            if (!response) {
//                System.out.println("response.. UNAUTHENTICATED");
//
//                throw new AppException(AppErrorCode.UNAUTHENTICATED);
//            }
//        } catch (JOSEException | ParseException e) {
//            System.out.println("response.. catch");
//            throw new AppException(AppErrorCode.UNAUTHENTICATED);
//        }
//
//        if (Objects.isNull(nimbusJwtDecoder)) {
//            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
//                    .macAlgorithm(MacAlgorithm.HS512)
//                    .build();
//        }
//
//        return nimbusJwtDecoder.decode(token);
//    }
@Override
public Jwt decode(String token) throws JwtException {
    try {
        var response = authenticationService.introspect(token , TokenType.ACCESS_TOKEN);
        if (!response) {
            System.out.println("response.. UNAUTHENTICATED");
            throw new RuntimeException("Unauthenticated");
        }
    } catch (JOSEException | ParseException e) {
        System.out.println("response.. catch");
        throw new RuntimeException("Unauthenticated", e);
    }

    if (Objects.isNull(nimbusJwtDecoder)) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    return nimbusJwtDecoder.decode(token);
}

}

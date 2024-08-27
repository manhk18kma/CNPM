package kma.cnpm.beapp.domain.user.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import kma.cnpm.beapp.domain.common.enumType.TokenType;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.user.dto.response.TokenResponse;
import kma.cnpm.beapp.domain.user.dto.resquest.LoginRequest;
import kma.cnpm.beapp.domain.user.dto.resquest.LogoutRequest;
import kma.cnpm.beapp.domain.user.dto.resquest.RefreshTokenRequest;
import kma.cnpm.beapp.domain.user.entity.InvalidatedToken;
import kma.cnpm.beapp.domain.user.entity.Permission;
import kma.cnpm.beapp.domain.user.entity.Role;
import kma.cnpm.beapp.domain.user.entity.User;
import kma.cnpm.beapp.domain.user.repository.InvalidateTokenRepository;
import kma.cnpm.beapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    @Value("${jwt.valid-duration}")
     long validDuration;

    @Value("${jwt.refreshable-duration}")
     long refreshableDuration;

    @Value("${jwt.reset-password-duration}")
     long resetPasswordDuration;

    @Value("${jwt.active-user-duration}")
     long activeUserDuration;

    @Value("${jwt.accessKey}")
     String accessKey;

    @Value("${jwt.refreshKey}")
     String refreshKey;

    @Value("${jwt.resetKey}")
     String resetPasswordKey;

    @Value("${jwt.activeKey}")
     String activeKey;

    final UserRepository userRepository;
    final InvalidateTokenRepository invalidateTokenRepository;
    final PasswordEncoder passwordEncoder;


    public String generateToken(User user , TokenType tokenType) {
        long validTime = getDuration(tokenType);
        String key = getKey(tokenType);
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("KMA_CNPM_DREAM_TEAM")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(validTime, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(key.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(AppErrorCode.INVALID_TOKEN_TYPE);
        }
    }


    public boolean introspect(String token , TokenType tokenType) throws JOSEException, ParseException {
        boolean isValid = true;
        try {
            verifyToken(token, tokenType);
        } catch (AppException e) {
            return false;
        }
        return true;
    }
//    public IntrospectResponse introspect(IntrospectRequest request, TokenType tokenType) throws JOSEException, ParseException {
//        var token = request.getToken();
//        boolean isValid = true;
//        try {
//            verifyToken(token, tokenType);
//        } catch (AppException e) {
//            isValid = false;
//        }
//        return IntrospectResponse.builder().valid(isValid).build();
//    }

    private SignedJWT verifyToken(String token, TokenType tokenType) throws JOSEException, ParseException {
        String key = getKey(tokenType);
        JWSVerifier verifier = new MACVerifier(key.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
        if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(AppErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String getKey(TokenType tokenType) {
        String key = "";
        switch (tokenType) {
            case ACCESS_TOKEN -> {
                key = accessKey;
            }
            case REFRESH_TOKEN -> {
                key = refreshKey;
            }
            case RESET_TOKEN -> {
                key = resetPasswordKey;
            }
            case ACTIVE_TOKEN ->{
                key = activeKey;
            }
            default -> throw new AppException(AppErrorCode.INVALID_TOKEN_TYPE);
        }
        return key;

    }
    private long getDuration(TokenType tokenType) {
        long validTime = 0;
        switch (tokenType) {
            case ACCESS_TOKEN -> {
                validTime = validDuration;
            }
            case REFRESH_TOKEN -> {
                validTime = refreshableDuration;
            }
            case RESET_TOKEN -> {
                validTime = resetPasswordDuration;
            }
            case ACTIVE_TOKEN ->{
                validTime = activeUserDuration;
            }
            default -> throw new AppException(AppErrorCode.INVALID_TOKEN_TYPE);
        }
        return validTime;

    }
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        List<Role> roles = userRepository.getRolesByUserId(user.getId());
        List<Permission> permissions = roles.stream()
                .flatMap(role -> userRepository.getPermissionByRoleId(role.getId()).stream())
                .collect(Collectors.toList());


        if (!CollectionUtils.isEmpty(roles))
            roles.forEach(role -> {
                stringJoiner.add("ROLE_" + role.getRoleName());
                if (!CollectionUtils.isEmpty(permissions))
                    permissions.forEach(permission -> stringJoiner.add(permission.getPermissionName()));
            });

        return stringJoiner.toString();
    }

    String extractEmail(String token, TokenType type) {
        SignedJWT signedJWT = null;
        try {
            signedJWT = verifyToken(token, type);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String username = null;
        try {
            username = signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return username;
    }

    String getJwtId(String token, TokenType tokenType) throws ParseException, JOSEException {
        var signedJWT = verifyToken(token, tokenType);
        return  signedJWT.getJWTClaimsSet().getJWTID();
    }

    public TokenResponse login(LoginRequest request) {
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(AppErrorCode.INVALID_USERNAME_PASSWORD));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

//        Login successfully -> add token device
        user.setTokenDevice(request.getDeviceToken());
        userRepository.save(user);
//
        if (!authenticated) throw new AppException(AppErrorCode.INVALID_USERNAME_PASSWORD);
//
        var token = generateToken(user , TokenType.ACCESS_TOKEN);
        var refreshToken = generateToken(user , TokenType.REFRESH_TOKEN);

        return TokenResponse.builder().accessToken(token).refreshToken(refreshToken).build();
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signedRefreshJWT = verifyToken(request.getRefreshToken(), TokenType.REFRESH_TOKEN);
//        if logged out the refresh token
        if (invalidateTokenRepository.existsById(signedRefreshJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
//        var signedAccessJWT = verifyToken(request.getAccessToken(), TokenType.ACCESS_TOKEN);
//
//        var jit = signedAccessJWT.getJWTClaimsSet().getJWTID();
//        var expiryTime = signedAccessJWT.getJWTClaimsSet().getExpirationTime();
//
//        InvalidatedToken invalidatedToken =
//                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
//
//        invalidateTokenRepository.save(invalidatedToken);
        var email = signedRefreshJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(AppErrorCode.UNAUTHENTICATED));
        var accessToken = generateToken(user , TokenType.ACCESS_TOKEN);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(request.getRefreshToken())
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
            // Verify the refresh token and access token
            var signedRefreshJWT = verifyToken(request.getRefreshToken(), TokenType.REFRESH_TOKEN);
            var signedAccessJWT = verifyToken(request.getAccessToken(), TokenType.ACCESS_TOKEN);

            // Process the refresh token
            String refreshJit = signedRefreshJWT.getJWTClaimsSet().getJWTID();
            var refreshExpiryTime = signedRefreshJWT.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedRefreshToken = InvalidatedToken.builder()
                    .id(refreshJit)
                    .expiryTime(refreshExpiryTime)
                    .build();

            invalidateTokenRepository.save(invalidatedRefreshToken);

            // Process the access token
            String accessJit = signedAccessJWT.getJWTClaimsSet().getJWTID();
            var accessExpiryTime = signedAccessJWT.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedAccessToken = InvalidatedToken.builder()
                    .id(accessJit)
                    .expiryTime(accessExpiryTime)
                    .build();

            invalidateTokenRepository.save(invalidatedAccessToken);
    }


    public String getAuthenticationName() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
        log.info("Username : {}", authentication.getName());
        return authentication.getName();
    }



}

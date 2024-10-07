package kma.cnpm.beapp.domain.user.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import kma.cnpm.beapp.domain.common.dto.UserDTO;
import kma.cnpm.beapp.domain.common.enumType.TokenType;
import kma.cnpm.beapp.domain.common.enumType.UserStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.common.notificationDto.ShipperDTO;
import kma.cnpm.beapp.domain.user.dto.response.TokenResponse;
import kma.cnpm.beapp.domain.user.dto.response.UserResponse;
import kma.cnpm.beapp.domain.user.dto.resquest.LoginRequest;
import kma.cnpm.beapp.domain.user.dto.resquest.LogoutRequest;
import kma.cnpm.beapp.domain.user.dto.resquest.RefreshTokenRequest;
import kma.cnpm.beapp.domain.user.entity.*;
import kma.cnpm.beapp.domain.user.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements CommandLineRunner {
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
    final RoleRepository roleRepository;
    final UserHasRoleRepository userHasRoleRepository;
    final FollowRepository followRepository;


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
//                .claim("scope", buildScope(user))
                .claim("roles", getRoles(user))
                .claim("authorities", getAuthorities(user))
                .claim("userId", user.getId().toString())
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
    private List<String> getRoles(User user) {
        List<Role> roles = userRepository.getRolesByUserId(user.getId());
        return roles.stream()
                .map(role -> "ROLE_" + role.getRoleName())
                .collect(Collectors.toList());
    }

    private List<String> getAuthorities(User user) {
        List<Role> roles = userRepository.getRolesByUserId(user.getId());
        return roles.stream()
                .flatMap(role -> userRepository.getPermissionByRoleId(role.getId()).stream())
                .map(Permission::getPermissionName)
                .collect(Collectors.toList());
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
        String saltedPassword = request.getPassword() + user.getSalt();
        boolean authenticated = passwordEncoder.matches(saltedPassword, user.getPassword());

        if (!authenticated) {
            throw new AppException(AppErrorCode.INVALID_USERNAME_PASSWORD);
        }
        user.setTokenDevice(request.getDeviceToken());
        userRepository.save(user);
        var token = generateToken(user, TokenType.ACCESS_TOKEN);
        var refreshToken = generateToken(user, TokenType.REFRESH_TOKEN);
        return TokenResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }


    public TokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signedRefreshJWT = verifyToken(request.getRefreshToken(), TokenType.REFRESH_TOKEN);
//        if logged out the refresh token
        if (invalidateTokenRepository.existsById(signedRefreshJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        var email = signedRefreshJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(AppErrorCode.UNAUTHENTICATED));
        var accessToken = generateToken(user , TokenType.ACCESS_TOKEN);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(request.getRefreshToken())
                .userId(user.getId())
                .build();
    }

    public UserResponse logout(LogoutRequest request) throws ParseException, JOSEException {

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
            return UserResponse.builder()
                    .userId(Long.valueOf(getAuthenticationName()))
                    .build();
    }


    public JWTClaimsSet extractClaims(String token, TokenType tokenType) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(token, tokenType);
        System.out.println(signedJWT.getJWTClaimsSet());
        return signedJWT.getJWTClaimsSet();
    }
    @SneakyThrows
    public String getAuthenticationName() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // Kiểm tra nếu không có authentication hoặc không phải là JwtAuthenticationToken
        if (authentication == null || !(authentication instanceof JwtAuthenticationToken)) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        String token = jwtAuth.getToken().getTokenValue();
        JWTClaimsSet jwtClaimsSet;
        try {
            jwtClaimsSet = extractClaims(token, TokenType.ACCESS_TOKEN);
        } catch (ParseException | JOSEException e) {
            log.error("Error extracting claims from token", e);
            throw new AppException(AppErrorCode.INVALID_TOKEN_TYPE);
        }
        String userId = jwtClaimsSet.getStringClaim("userId");
        return userId;
    }

    @SneakyThrows
    public String authNameCanBeNull() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication instanceof JwtAuthenticationToken)) {
            return null;
        }
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
        String token = jwtAuth.getToken().getTokenValue();
        JWTClaimsSet jwtClaimsSet;
        try {
            jwtClaimsSet = extractClaims(token, TokenType.ACCESS_TOKEN);
        } catch (ParseException | JOSEException e) {
            log.error("Error extracting claims from token", e);
            throw new AppException(AppErrorCode.INVALID_TOKEN_TYPE);
        }
        String userId = jwtClaimsSet.getStringClaim("userId");
        return userId;
    }


    @Override
    public void run(String... args) throws Exception {
        // Khởi tạo các vai trò nếu chưa tồn tại
        createRoleIfNotExists("USER", "Người dùng bình thường");
        createRoleIfNotExists("ADMIN", "Quản trị viên hệ thống");
        createRoleIfNotExists("SHIPPER", "Người giao hàng");

        // Khởi tạo tài khoản admin nếu chưa tồn tại
        createUserIfNotExists("admin@gmail.com", "admin", "ADMIN");

        // Khởi tạo tài khoản shipper nếu chưa tồn tại
        createUserIfNotExists("shipper@gmail.com", "shipper", "SHIPPER");
    }

    private void createRoleIfNotExists(String roleName, String description) {
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .description(description)
                    .build();
            roleRepository.save(role);
        }
    }


    private void createUserIfNotExists(String email, String password, String roleName) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            String salt = generateSalt();
            String encodedPassword = encodePassword(password, salt);

            User newUser = User.builder()
                    .email(email)
                    .status(UserStatus.ACTIVE)
                    .salt(salt)
                    .password(encodedPassword)
                    .build();
            userRepository.save(newUser);

            Role role = roleRepository.findByRoleName(roleName);
            if (role != null) {
                UserHasRole userHasRole = UserHasRole.builder()
                        .role(role)
                        .user(newUser)
                        .build();
                userHasRoleRepository.save(userHasRole);
            } else {
                throw new RuntimeException("Role '" + roleName + "' not found");
            }
        }
    }
    private String encodePassword(String rawPassword, String salt) {
        return passwordEncoder.encode(rawPassword + salt);
    }
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new String(salt);
    }


    public List<ShipperDTO> getTokenDeviceByRoleName(String roleName) {
        return userRepository.getTokenDeviceShipper(roleName).stream()
                .map(user -> {
                    return ShipperDTO.builder()
                            .tokenDevice(user.getTokenDevice())
                            .id(user.getId())
                            .build();
                }).collect(Collectors.toList());
    }


    public UserDTO getUserInfo(Long id){
        User user = userRepository.findUserById((id))
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));
        return UserDTO.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .build();
    }

    public List<UserDTO> getFollowersOfUser(Long userId) {
        List<User> followers = followRepository.getUserFollowersOfUser(userId);
        return followers.stream()
                .map(user->{
                    return UserDTO.builder()
                            .userId(user.getId())
                            .tokenDevice(user.getTokenDevice())
                            .build();
                }).collect(Collectors.toList());
    }
    public String getTokenDeviceByUserId(Long userId){
        return userRepository.getTokenDeviceByUserId(userId);
    }
}

package kma.cnpm.beapp.domain.user.service;

import com.nimbusds.jose.JOSEException;
import kma.cnpm.beapp.domain.common.enumType.Gender;
import kma.cnpm.beapp.domain.common.enumType.TokenType;
import kma.cnpm.beapp.domain.common.enumType.UserStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.user.dto.response.TokenResponse;
import kma.cnpm.beapp.domain.user.dto.response.UserResponse;
import kma.cnpm.beapp.domain.user.dto.resquest.*;
import kma.cnpm.beapp.domain.user.entity.ActiveResetToken;
import kma.cnpm.beapp.domain.user.entity.User;
import kma.cnpm.beapp.domain.user.repository.ActiveResetTokenRepository;
import kma.cnpm.beapp.domain.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    @Value("${urlClient}")
    String urlClient;
    @Value("${recaptcha.secret-key}")
    private String secretKey;
    @Value("${recaptcha.verify-url}")
    private  String VERIFY_URL;
    final UserRepository userRepository;
    final NotificationService notificationService;
     final AuthService authService;
    final ActiveResetTokenRepository activeResetTokenRepository;
    final PasswordEncoder passwordEncoder;
    @Transactional
    public UserResponse saveUser(CreateUserRequest request) {
        String email = request.getEmail();
        String phone = request.getPhone();

        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw  new AppException(AppErrorCode.Passwords_Not_Match);
        }

        checkIfExists(userRepository.existsUserByEmail(email), AppErrorCode.EMAIL_IS_USED);
        checkIfExists(userRepository.existsUserByPhone(phone), AppErrorCode.PHONE_IS_USED);

        List<User> inactiveUsers = userRepository.findByUsernameNotActivateByPhoneEmailUsername(email, phone);
        inactiveUsers.forEach(user -> {
            activeResetTokenRepository.deleteTokenBySub(user.getEmail() , TokenType.ACTIVE_TOKEN);
            userRepository.delete(user);
        });

        // Tạo và lưu người dùng mới
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setStatus(UserStatus.INACTIVE);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        // Tạo token kích hoạt và liên kết kích hoạt
        String activeToken = authService.generateToken(savedUser, TokenType.ACTIVE_TOKEN);
        String activateLink = urlClient + "/activate?token=" + activeToken;
        String subject = "Account Activation";

        // Lưu token và gửi email kích hoạt
        ActiveResetToken activeResetToken = ActiveResetToken.builder()
                .tokenType(TokenType.ACTIVE_TOKEN)
                .sub(email)
                .build();
        activeResetTokenRepository.save(activeResetToken);

        notificationService.sendActivationEmail(email, subject, activateLink);

        return UserResponse.builder().id(savedUser.getId()).build();
    }


    public boolean submitCaptcha(String captchaToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?secret=%s&response=%s", VERIFY_URL, secretKey, captchaToken);
        String response = restTemplate.getForObject(url, String.class);

        System.out.println(response);
        if (response != null && response.contains("\"success\": true")) {
            return true;
        } else {
            return false;
        }
    }


    private void checkIfExists(boolean exists, AppErrorCode errorCode) {
        if (exists) {
            throw new AppException(errorCode);
        }
    }

    @Transactional
    public TokenResponse activateUser(ActiveUserRequest request) throws ParseException, JOSEException {
        String username = authService.extractUsername(request.getToken(), TokenType.ACTIVE_TOKEN);

        activeResetTokenRepository.findTokenBySub(username,TokenType.ACTIVE_TOKEN)
                .orElseThrow(() -> new AppException(AppErrorCode.UNAUTHENTICATED));

        User user = userRepository.findByUsernameNotActivate(username)
                .orElseThrow(() -> new AppException(AppErrorCode.USERNAME_NOT_EXISTED));

        if (user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new AppException(AppErrorCode.USER_ACTIVATED);
        }

        user.setStatus(UserStatus.ACTIVE);
        User savedUser = userRepository.save(user);

        activeResetTokenRepository.deleteTokenBySub(username, TokenType.ACTIVE_TOKEN);

        String accessToken = authService.generateToken(savedUser, TokenType.ACCESS_TOKEN);

        var refreshToken = authService.generateToken(savedUser, TokenType.REFRESH_TOKEN);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void forgotPassword(ForgotPassRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(AppErrorCode.EMAIL_NOT_EXISTED));

        String resetToken = authService.generateToken(user, TokenType.RESET_TOKEN);
        String resetLink =urlClient + "/reset-password?token=" + resetToken;
        String subject = "Reset Password";

        activeResetTokenRepository.deleteTokenBySub(user.getEmail(), TokenType.RESET_TOKEN);

        ActiveResetToken activeResetToken = ActiveResetToken.builder()
                .tokenType(TokenType.RESET_TOKEN)
                .sub(user.getEmail())
                .build();
        activeResetTokenRepository.save(activeResetToken);

        notificationService.sendResetPasswordEmail(user.getEmail(), subject, resetLink);
    }

    @Transactional
    public TokenResponse resetPassword(ResetPasswordRequest request) {
        String username = authService.extractUsername(request.getSecretKey(), TokenType.RESET_TOKEN);

        activeResetTokenRepository.findTokenBySub(username, TokenType.RESET_TOKEN)
                .orElseThrow(() -> new AppException(AppErrorCode.UNAUTHENTICATED));

        User user = findUserByUsername(username);

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(AppErrorCode.Passwords_Not_Match);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        activeResetTokenRepository.deleteTokenBySub(username, TokenType.RESET_TOKEN);

        String accessToken = authService.generateToken(savedUser, TokenType.ACCESS_TOKEN);
        String refreshToken = authService.generateToken(savedUser, TokenType.REFRESH_TOKEN);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Transactional
    public UserResponse updateUser(UpdateUserRequest request) throws ParseException {
        String username = authService.getAuthenticationName();
        User user = findUserByUsername(username);
        checkIfExists(userRepository.existsUserByPhone(request.getPhone()), AppErrorCode.PHONE_IS_USED);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(request.getDateOfBirth());

        user.setFullName(request.getFullName());
        user.setGender(Gender.valueOf(request.getGender()));
        user.setDateOfBirth(date);
        user.setPhone(request.getPhone());

        return UserResponse.builder()
                .id(userRepository.save(user).getId())
                .build();

    }




    User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(AppErrorCode.USERNAME_NOT_EXISTED));
    }
}

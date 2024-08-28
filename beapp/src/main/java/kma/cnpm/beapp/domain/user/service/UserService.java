package kma.cnpm.beapp.domain.user.service;

import com.nimbusds.jose.JOSEException;
import kma.cnpm.beapp.domain.common.enumType.Gender;
import kma.cnpm.beapp.domain.common.enumType.TokenType;
import kma.cnpm.beapp.domain.common.enumType.UserStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.common.upload.ImageService;
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
import java.util.Calendar;
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
    final ImageService imageService;

//    Register new user
    @Transactional
    public UserResponse saveUser(CreateUserRequest request) {
        if (!submitCaptcha(request.getCaptchaToken())){
            throw  new AppException(AppErrorCode.CAPTCHA_INVALID);
        }
        String email = request.getEmail();
        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw  new AppException(AppErrorCode.PASSWORDS_NOT_MATCH);
        }
//        Remove expired register time or other user is in process time
        List<User> inactiveUsers = userRepository.findUsersByEmail(email);
        inactiveUsers.forEach(user -> {
            if (UserStatus.ACTIVE.equals(user.getStatus())) {
                throw new AppException(AppErrorCode.EMAIL_IS_USED);
            }
            if (isExpireTime(user.getCreatedAt())) {
                userRepository.delete(user);
            } else {
                throw new AppException(AppErrorCode.EMAIL_IS_IN_PROCESS);
            }
        });



        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setStatus(UserStatus.INACTIVE);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

//        Send active link
        String activeToken = authService.generateToken(savedUser, TokenType.ACTIVE_TOKEN);
        String activateLink = urlClient + "/activate?token=" + activeToken;
        String subject = "Account Activation";
        notificationService.sendActivationEmail(user.getEmail(), subject, activateLink);
        return UserResponse.builder().id(savedUser.getId()).build();
    }

//    Check register process in 3 minutes
    private boolean isExpireTime(Date createdAt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdAt);
        calendar.add(Calendar.MINUTE, 3);

        Date createdAtPlus3Minutes = calendar.getTime();
        Date now = new Date();
        return createdAtPlus3Minutes.before(now);
    }
//    Validate captcha
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
    public TokenResponse activateUser(ActiveUserRequest request) {
        String email = authService.extractEmail(request.getToken(), TokenType.ACTIVE_TOKEN);

//        Email not existed or is activated
        User user = userRepository.findByEmailActivateOrInactive(email)
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));
        if (user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new AppException(AppErrorCode.USER_ACTIVATED);
        }
        user.setStatus(UserStatus.ACTIVE);
        user.setTokenDevice(request.getTokenDevice());
        User savedUser = userRepository.save(user);

//        Generate access , refresh token
        String accessToken = authService.generateToken(savedUser, TokenType.ACCESS_TOKEN);
        String refreshToken = authService.generateToken(savedUser, TokenType.REFRESH_TOKEN);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

//    Generate reset password token
    @Transactional
    public void forgotPassword(ForgotPassRequest request) throws ParseException, JOSEException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(AppErrorCode.EMAIL_NOT_EXISTED));

//        Remove all last token ensure that in db always 1 reset token and it is the latest token
        activeResetTokenRepository.deleteTokenBySub(user.getEmail(), TokenType.RESET_TOKEN);
        String resetToken = authService.generateToken(user, TokenType.RESET_TOKEN);
        String resetLink =urlClient + "/reset-password?token=" + resetToken;
        String subject = "Reset Password";
        ActiveResetToken activeResetToken = ActiveResetToken.builder()
                .tokenType(TokenType.RESET_TOKEN)
                .sub(user.getEmail())
                .jwtId(authService.getJwtId(resetToken , TokenType.RESET_TOKEN))
                .build();
        activeResetTokenRepository.save(activeResetToken);
        notificationService.sendResetPasswordEmail(user.getEmail(), subject, resetLink);
    }


    @Transactional
    public TokenResponse resetPassword(ResetPasswordRequest request) throws ParseException, JOSEException {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(AppErrorCode.PASSWORDS_NOT_MATCH);
        }
//       Find only token in db and after use delete to ensure can can change password once
        String email = authService.extractEmail(request.getSecretKey(), TokenType.RESET_TOKEN);
        String jwtId = authService.getJwtId(request.getSecretKey() , TokenType.RESET_TOKEN);

        activeResetTokenRepository.findResetTokenBySubAndJwtId(jwtId, email, TokenType.RESET_TOKEN)
                .orElseThrow(() -> new AppException(AppErrorCode.UNAUTHENTICATED));

        User user = findUserByEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTokenDevice(request.getTokenDevice());
        User savedUser = userRepository.save(user);

//        Remove after used
        activeResetTokenRepository.deleteTokenBySub(email, TokenType.RESET_TOKEN);

        String accessToken = authService.generateToken(savedUser, TokenType.ACCESS_TOKEN);
        String refreshToken = authService.generateToken(savedUser, TokenType.REFRESH_TOKEN);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Transactional
    public UserResponse updateUser(UpdateUserRequest request) throws ParseException {
        String email = authService.getAuthenticationName();
        User user = findUserByEmail(email);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(request.getDateOfBirth());

        String urlOrBase64 = request.getUrlAvtOrBase64();
        String oldUrlAvt = user.getAvt();

        if (urlOrBase64 != null && !urlOrBase64.isEmpty() && !urlOrBase64.startsWith("http")) {
            try {
                if (oldUrlAvt != null) {
                    imageService.deleteImage(oldUrlAvt);
                }
                String newUrl = imageService.getUrlImage(urlOrBase64);
                user.setAvt(newUrl);
            } catch (Exception e) {
                System.out.println("-----------");
                setDefaultAvatar(user, oldUrlAvt);
            }
        } else {
            setDefaultAvatar(user, oldUrlAvt);
        }

        user.setFullName(request.getFullName());
        user.setGender(Gender.valueOf(request.getGender()));
        user.setDateOfBirth(date);
        user.setPhone(request.getPhone());

        return UserResponse.builder()
                .id(userRepository.save(user).getId())
                .build();
    }

    private void setDefaultAvatar(User user, String oldUrlAvt) {
        if (oldUrlAvt != null) {
            imageService.deleteImage(oldUrlAvt);
        }
        user.setAvt("https://tse1.mm.bing.net/th?id=OIP._prlVvISXU3EfqFW3GF-RwHaHa&pid=Api&P=0&h=220");
    }



     public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));
    }



}

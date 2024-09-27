package kma.cnpm.beapp.domain.user.service;

import com.nimbusds.jose.JOSEException;
import kma.cnpm.beapp.domain.common.dto.UserDTO;
import kma.cnpm.beapp.domain.common.enumType.Gender;
import kma.cnpm.beapp.domain.common.enumType.TokenType;
import kma.cnpm.beapp.domain.common.enumType.UserStatus;
import kma.cnpm.beapp.domain.common.exception.AppErrorCode;
import kma.cnpm.beapp.domain.common.exception.AppException;
import kma.cnpm.beapp.domain.common.upload.ImageService;
import kma.cnpm.beapp.domain.payment.service.AccountService;
import kma.cnpm.beapp.domain.user.dto.response.TokenResponse;
import kma.cnpm.beapp.domain.user.dto.response.UserResponse;
import kma.cnpm.beapp.domain.user.dto.resquest.*;
import kma.cnpm.beapp.domain.user.entity.ActiveResetToken;
import kma.cnpm.beapp.domain.user.entity.Role;
import kma.cnpm.beapp.domain.user.entity.User;
import kma.cnpm.beapp.domain.user.entity.UserHasRole;
import kma.cnpm.beapp.domain.user.repository.ActiveResetTokenRepository;
import kma.cnpm.beapp.domain.user.repository.RoleRepository;
import kma.cnpm.beapp.domain.user.repository.UserHasRoleRepository;
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
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    @Value("${urlDefaultAvt}")
    private String urlDefaultAvt;
    final UserRepository userRepository;
    final NotificationService notificationService;
     final AuthService authService;
    final ActiveResetTokenRepository activeResetTokenRepository;
    final PasswordEncoder passwordEncoder;
    final ImageService imageService;
    final AccountService accountService;
    final RoleRepository roleRepository;
    final UserHasRoleRepository userHasRoleRepository;

//    Register new user
    @Transactional
    public UserResponse saveUser(CreateUserRequest request) {
//        if (!submitCaptcha(request.getCaptchaToken())){
//            throw  new AppException(AppErrorCode.CAPTCHA_INVALID);
//        }
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
        user.setAvt(urlDefaultAvt);
        //hanldepassword
        String salt = generateSalt();
        String encodedPassword = encodePassword(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);

        Role roleUser = roleRepository.findByRoleName("USER");
        UserHasRole userHasRole = UserHasRole.builder()
                .user(savedUser)
                .role(roleUser)
                .build();
        userHasRoleRepository.save(userHasRole);

//        Send active link
        String activeToken = authService.generateToken(savedUser, TokenType.ACTIVE_TOKEN);
        String activateLink = urlClient + "/active/" + activeToken;
        String subject = "Account Activation";
        notificationService.sendActivationEmail(user.getEmail(), subject, activateLink);
        return UserResponse.builder().userId(savedUser.getId()).build();
    }

//    Check register process in 3 minutes
private boolean isExpireTime(LocalDateTime createdAt) {
    LocalDateTime createdAtPlus3Minutes = createdAt.plusMinutes(3);
    LocalDateTime now = LocalDateTime.now();
    return createdAtPlus3Minutes.isBefore(now);
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
        accountService.initAccount(savedUser.getId());

//        Generate access , refresh token
        String accessToken = authService.generateToken(savedUser, TokenType.ACCESS_TOKEN);
        String refreshToken = authService.generateToken(savedUser, TokenType.REFRESH_TOKEN);
        return TokenResponse.builder()
                .userId(user.getId())
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
        String salt = user.getSalt();

        String encodedPassword = encodePassword(request.getPassword(), salt);
        user.setPassword(encodedPassword);
        user.setTokenDevice(request.getTokenDevice());
        User savedUser = userRepository.save(user);
//        Remove after used
        activeResetTokenRepository.deleteTokenBySub(email, TokenType.RESET_TOKEN);

        String accessToken = authService.generateToken(savedUser, TokenType.ACCESS_TOKEN);
        String refreshToken = authService.generateToken(savedUser, TokenType.REFRESH_TOKEN);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }


    @Transactional
    public UserResponse updateUser(UpdateUserRequest request) throws ParseException {
        String id = authService.getAuthenticationName();
        User user = findUserById(id);
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
                setDefaultAvatar(user, oldUrlAvt);
            }
        }

        user.setFullName(request.getFullName());
        user.setGender(Gender.valueOf(request.getGender()));
        user.setDateOfBirth(date);
        user.setPhone(request.getPhone());

        return UserResponse.builder()
                .userId(userRepository.save(user).getId())
                .build();
    }





    ///Helper function

    public String encodePassword(String rawPassword, String salt) {
        return passwordEncoder.encode(rawPassword + salt);
    }
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new String(salt);
    }

    private void setDefaultAvatar(User user, String oldUrlAvt) {
        if (oldUrlAvt != null) {
            imageService.deleteImage(oldUrlAvt);
        }
        user.setAvt(urlDefaultAvt);
    }



     public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));
    }

    public User findUserById(String id) {
        return userRepository.findUserById(Long.valueOf(id))
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));
    }

    public UserDTO getUserInfo(String id){
        User user = userRepository.findUserById(Long.valueOf(id))
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));
        return UserDTO.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .build();
    }



}

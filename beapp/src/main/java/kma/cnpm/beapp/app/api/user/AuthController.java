package kma.cnpm.beapp.app.api.user;

import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.common.enumType.TokenType;
import kma.cnpm.beapp.domain.user.dto.response.TokenResponse;
import kma.cnpm.beapp.domain.user.dto.resquest.*;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "APIs for authentication and user management")
public class AuthController {

    AuthService authService;
    UserService userService;

    @Operation(summary = "User login", description = "Authenticate a user with their credentials and return an access token.")
    @PostMapping("/login")
    public ResponseData<TokenResponse> login(
            @Parameter(description = "Login request payload containing username and password", required = true)
            @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Login successfully",
                new Date(),
                response);
    }

    @Operation(summary = "Refresh token", description = "Refresh an existing access token using a refresh token.")
    @PostMapping("/tokens-refresh")
    public ResponseData<TokenResponse> refreshToken(
            @Parameter(description = "Request payload containing the refresh token to obtain a new access token", required = true)
            @RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        TokenResponse response = authService.refreshToken(request);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Refresh token successfully",
                new Date(),
                response);
    }

    @Operation(summary = "Logout", description = "Invalidate the current refresh token and logout the user.")
    @PostMapping("/logout")
    public ResponseData<Void> logout(
            @Parameter(description = "Request payload containing the refresh token for logout", required = true)
            @RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(),
                "Logout successfully",
                new Date());
    }

    @Operation(summary = "Forgot password", description = "Send a password reset link to the user's email address.")
    @PostMapping("/passwords-forgot")
    public ResponseData<Void> forgotPassword(
            @Parameter(description = "Request payload containing email for password recovery", required = true)
            @RequestBody ForgotPassRequest request) throws ParseException, JOSEException {
        userService.forgotPassword(request);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(),
                "Please check your email for password reset instructions.",
                new Date());
    }

    @Operation(summary = "Reset password", description = "Reset the user's password using a reset token and new password.")
    @PostMapping("/passwords-reset")
    public ResponseData<TokenResponse> resetPassword(
            @Parameter(description = "Request payload containing new password and reset token", required = true)
            @RequestBody ResetPasswordRequest request) throws ParseException, JOSEException {
        TokenResponse response = userService.resetPassword(request);
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Password reset successfully",
                new Date(),
                response);
    }


    @Operation(summary = "Introspect token", description = "Check the validity and details of the provided token.")
    @PostMapping("/tokens-introspect")
    public ResponseData<Boolean> introspect(
            @Parameter(description = "Request payload containing the token to introspect", required = true)
            @RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        boolean isValid = authService.introspect(request.getToken(), TokenType.ACCESS_TOKEN);
        return new ResponseData<>(HttpStatus.OK.value(),
                "Token introspected successfully",
                new Date(),
                isValid);
    }
}

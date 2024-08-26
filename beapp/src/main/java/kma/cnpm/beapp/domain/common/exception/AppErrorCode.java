package kma.cnpm.beapp.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum AppErrorCode {
    USERNAME_IS_USED(1, "Username has been used", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_PASSWORD(2, "Username or password incorrect", HttpStatus.BAD_REQUEST),
    EMAIL_IS_USED(3, "Email has been used", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(4, "Email not existed", HttpStatus.BAD_REQUEST),
    EMAIL_IS_IN_PROCESS(5, "Email is in process, please try again", HttpStatus.BAD_REQUEST),
    PHONE_IS_USED(6, "Phone has been used", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN_TYPE(7, "Invalid token type", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(8, "Username not existed", HttpStatus.BAD_REQUEST),
    USER_ACTIVATED(9, "User has been activated", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_EXISTED(10, "Address not existed", HttpStatus.BAD_REQUEST),
    PASSWORDS_NOT_MATCH(11, "Password and confirm password do not match", HttpStatus.BAD_REQUEST),

    // Implementation error codes
    UNCATEGORIZED_EXCEPTION(1999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    CAPTCHA_INVALID(12,"Captcha invalid" , HttpStatus.BAD_REQUEST);


    AppErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}

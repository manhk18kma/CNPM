package kma.cnpm.beapp.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum AppErrorCode {
    USERNAME_IS_USED(1 , "Username has been used" , HttpStatus.BAD_REQUEST),
    EMAIL_IS_USED(2, "Email has been used" , HttpStatus.BAD_REQUEST),
    PHONE_IS_USED(3 , "Phone has been used" , HttpStatus.BAD_REQUEST),
    INVALID_TOKEN_TYPE(3 , "Invalid token type has been used" , HttpStatus.BAD_REQUEST),
    USERNAME_NOT_EXISTED(3 , "Username not existed" , HttpStatus.BAD_REQUEST),
    USER_ACTIVATED(5,"User has been activated" , HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_PASSWORD(1, "Username or password incorrect" , HttpStatus.BAD_REQUEST),

    Passwords_Not_Match(6,"Password and confirm password not match" ,HttpStatus.BAD_REQUEST ),
    EMAIL_NOT_EXISTED(7,"Email not existed" ,HttpStatus.BAD_REQUEST ),
    //    Impl error code
    UNCATEGORIZED_EXCEPTION(1999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN);

    AppErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}

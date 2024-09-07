package kma.cnpm.beapp.domain.common.exception;

import kma.cnpm.beapp.domain.common.enumType.WithdrawalStatus;
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
    ACCOUNT_NOT_EXIST(14 , "Account not existed" , HttpStatus.BAD_REQUEST),


    // Implementation error codes
    UNCATEGORIZED_EXCEPTION(1999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    CAPTCHA_INVALID(12,"Captcha invalid" , HttpStatus.BAD_REQUEST),



    CATEGORY_NOT_EXISTED(2001, "Category not existed", HttpStatus.BAD_REQUEST),
    PAYMENT_GATEWAY_NOT_EXIST(15,"PaymentGateway not existed" ,HttpStatus.BAD_REQUEST ),
    VNPAY_SIGNING_FAILED(16,"Vn pay singe fail" ,HttpStatus.INTERNAL_SERVER_ERROR  ),
    TRANSACTION_NOT_EXISTED(17,"Transaction not existed" , HttpStatus.BAD_REQUEST),
    BANK_NOT_EXIST(18,"Bank not existed" ,HttpStatus.BAD_REQUEST ),
    ACCOUNT_HAS_BANK_EXIST(19,"Bank has been added to account" , HttpStatus.BAD_REQUEST ),
    ACCOUNT_HAS_BANK_NOT_EXIST(20,"Bank not exsiting in this account" ,HttpStatus.BAD_REQUEST ),
    BALANCE_NOT_ENOUGH(21, "Balance not enough", HttpStatus.BAD_REQUEST),
    PENDING_WITHDRAWAL_ALREADY_EXISTS(22, "A pending withdrawal already exists for this account", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_NOT_EXIST(23,"Withdrawal not exist" ,HttpStatus.BAD_REQUEST),
    WITHDRAWAL_APPROVED_ERROR(24, "Withdrawal has been APPROVED", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_REJECTED_ERROR(25, "Withdrawal has been REJECTED", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_CANCELLED_ERROR(26, "Withdrawal has been CANCELLED", HttpStatus.BAD_REQUEST),



    PRODUCT_NOT_EXISTED(3001, "Product not existed", HttpStatus.BAD_REQUEST),
    POST_NOT_EXISTED(4001, "Post not existed", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_EXISTED(4001, "comment not existed", HttpStatus.BAD_REQUEST),

    FAILD_UPLOAD_CLOUD(13,"Error  to upload cloud" ,HttpStatus.BAD_REQUEST );

    AppErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;




}

package kma.cnpm.beapp.domain.common.exception;

import kma.cnpm.beapp.domain.common.enumType.WithdrawalStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum AppErrorCode {
    // User-related errors
    USERNAME_IS_USED(1, "Username has been used", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_PASSWORD(2, "Username or password incorrect", HttpStatus.BAD_REQUEST),
    EMAIL_IS_USED(3, "Email has been used", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(4, "Email not existed", HttpStatus.BAD_REQUEST),
    EMAIL_IS_IN_PROCESS(5, "Email is in process, please try again", HttpStatus.BAD_REQUEST),
    PHONE_IS_USED(6, "Phone has been used", HttpStatus.BAD_REQUEST),
    
    // Payment-related errors
    PAYMENT_GATEWAY_NOT_EXIST(15, "PaymentGateway not existed", HttpStatus.BAD_REQUEST),
    VNPAY_SIGNING_FAILED(16, "VnPay signing failed", HttpStatus.INTERNAL_SERVER_ERROR),
    TRANSACTION_NOT_EXISTED(17, "Transaction not existed", HttpStatus.BAD_REQUEST),
    BANK_NOT_EXIST(18, "Bank not existed", HttpStatus.BAD_REQUEST),
    ACCOUNT_HAS_BANK_EXIST(19, "Bank has been added to account", HttpStatus.BAD_REQUEST),
    ACCOUNT_HAS_BANK_NOT_EXIST(20, "Bank not existing in this account", HttpStatus.BAD_REQUEST),
    BALANCE_NOT_ENOUGH(21, "Balance not enough", HttpStatus.BAD_REQUEST),
    PENDING_WITHDRAWAL_ALREADY_EXISTS(22, "A pending withdrawal already exists for this account", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_NOT_EXIST(23, "Withdrawal not exist", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_APPROVED_ERROR(24, "Withdrawal has been APPROVED", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_REJECTED_ERROR(25, "Withdrawal has been REJECTED", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_CANCELLED_ERROR(26, "Withdrawal has been CANCELLED", HttpStatus.BAD_REQUEST),

    // Product-related errors
    PRODUCT_NOT_EXISTED(3001, "Product not existed", HttpStatus.BAD_REQUEST),
    POST_NOT_EXISTED(4001, "Post not existed", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_EXISTED(4001, "Comment not existed", HttpStatus.BAD_REQUEST),
    
    // Media upload errors
    FAILD_UPLOAD_CLOUD(13, "Error to upload cloud", HttpStatus.BAD_REQUEST);

    AppErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;




}

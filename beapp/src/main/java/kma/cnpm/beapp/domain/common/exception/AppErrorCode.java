package kma.cnpm.beapp.domain.common.exception;

import kma.cnpm.beapp.domain.common.enumType.WithdrawalStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum AppErrorCode {
    USERNAME_IS_USED(1, "Tên người dùng đã được sử dụng", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_PASSWORD(2, "Tên người dùng hoặc mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    EMAIL_IS_USED(3, "Email đã được sử dụng", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(4, "Email không tồn tại", HttpStatus.BAD_REQUEST),
    EMAIL_IS_IN_PROCESS(5, "Email đang được xử lý, vui lòng thử lại", HttpStatus.BAD_REQUEST),
    PHONE_IS_USED(6, "Số điện thoại đã được sử dụng", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN_TYPE(7, "Loại token không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(8, "Tên người dùng không tồn tại", HttpStatus.BAD_REQUEST),
    USER_ACTIVATED(9, "Người dùng đã được kích hoạt", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_EXISTED(10, "Địa chỉ không tồn tại", HttpStatus.BAD_REQUEST),
    PASSWORDS_NOT_MATCH(11, "Mật khẩu và xác nhận mật khẩu không khớp", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_EXIST(14, "Tài khoản không tồn tại", HttpStatus.BAD_REQUEST),
    USER_ALREADY_FOLLOWED(15, "Người dùng đã được theo dõi", HttpStatus.BAD_REQUEST),
    FOLLOW_NOT_EXIST(16, "Theo dõi không tồn tại", HttpStatus.BAD_REQUEST),

    // Implementation error codes
    UNCATEGORIZED_EXCEPTION(1999, "Lỗi không phân loại", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1007, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    CAPTCHA_INVALID(12, "Captcha không hợp lệ", HttpStatus.BAD_REQUEST),

    CATEGORY_NOT_EXISTED(2001, "Danh mục không tồn tại", HttpStatus.BAD_REQUEST),
    PAYMENT_GATEWAY_NOT_EXIST(15, "Cổng thanh toán không tồn tại", HttpStatus.BAD_REQUEST),
    VNPAY_SIGNING_FAILED(16, "Lỗi ký VNPay", HttpStatus.INTERNAL_SERVER_ERROR),
    TRANSACTION_NOT_EXISTED(17, "Giao dịch không tồn tại", HttpStatus.BAD_REQUEST),
    BANK_NOT_EXIST(18, "Ngân hàng không tồn tại", HttpStatus.BAD_REQUEST),
    ACCOUNT_HAS_BANK_EXIST(19, "Ngân hàng đã được thêm vào tài khoản", HttpStatus.BAD_REQUEST),
    ACCOUNT_HAS_BANK_NOT_EXIST(20, "Ngân hàng không tồn tại trong tài khoản này", HttpStatus.BAD_REQUEST),
    BALANCE_NOT_ENOUGH(21, "Số dư không đủ", HttpStatus.BAD_REQUEST),
    PENDING_WITHDRAWAL_ALREADY_EXISTS(22, "Đã có yêu cầu rút tiền đang chờ xử lý cho tài khoản này", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_NOT_EXIST(23, "Yêu cầu rút tiền không tồn tại", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_APPROVED_ERROR(24, "Yêu cầu rút tiền đã được DUYỆT", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_REJECTED_ERROR(25, "Yêu cầu rút tiền đã bị TỪ CHỐI", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_CANCELLED_ERROR(26, "Yêu cầu rút tiền đã bị HỦY", HttpStatus.BAD_REQUEST),

    PRODUCT_NOT_EXISTED(3001, "Sản phẩm không tồn tại", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_IN_STOCK(3002, "Not enough products in stock", HttpStatus.BAD_REQUEST),

    POST_NOT_EXISTED(4001, "Bài viết không tồn tại", HttpStatus.BAD_REQUEST),
    LIKE_EXISTED(4003, "Bài viết đã được thích", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_EXISTED(4002, "Bình luận không tồn tại", HttpStatus.BAD_REQUEST),

    ORDER_NOT_EXISTED(5001, "Đơn hàng không tồn tại", HttpStatus.BAD_REQUEST),
    ORDER_ITEM_NOT_EXISTED(5002, "Vật phẩm của đơn hàng không tồn tại", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_EXISTED(5003, "Vật phẩm của giỏ hàng không tồn tại", HttpStatus.BAD_REQUEST),



    FAILD_UPLOAD_CLOUD(13, "Lỗi khi tải lên đám mây", HttpStatus.BAD_REQUEST),
    INVALID_PARAM(27, "Tham số không hợp lệ", HttpStatus.BAD_REQUEST);

    AppErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}

package kma.cnpm.beapp.domain.payment.service;

import kma.cnpm.beapp.domain.common.enumType.Currency;
import kma.cnpm.beapp.domain.common.enumType.Locale;
import kma.cnpm.beapp.domain.payment.constant.Symbol;
import kma.cnpm.beapp.domain.payment.constant.VNPayParams;
import kma.cnpm.beapp.domain.payment.dto.request.InitDepositRequest;
import kma.cnpm.beapp.domain.payment.dto.response.InitDepositResponse;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VnpayService {
    public static final String VERSION = "2.1.0";
    public static final String COMMAND = "pay";
    public static final String ORDER_TYPE = "190000";
    public static final long DEFAULT_MULTIPLIER = 100L;

    @Value("${payment.vnpay.tmn-code}")
    private String tmnCode;

    @Value("${payment.vnpay.init-payment-url}")
    private String initPaymentPrefixUrl;

    @Value("${payment.vnpay.return-url}")
    private String returnUrlFormat;

    @Value("${payment.vnpay.timeout}")
    private Integer paymentTimeout;

    final CryptoService cryptoService;

    protected static final SimpleDateFormat VNPAY_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");


    public InitDepositResponse init(InitDepositRequest request) {
        // Nhân số tiền trong request với hệ số mặc định (DEFAULT_MULTIPLIER) để tính số tiền thanh toán
        var amount = request.getAmount() * DEFAULT_MULTIPLIER;

        // Lấy mã giao dịch từ request
        var txnRef = request.getTxnRef();

        // Xây dựng URL trả về khi hoàn thành giao dịch
        var returnUrl = buildReturnUrl(String.valueOf(txnRef));

        // Lấy thời gian hiện tại theo múi giờ GMT+7
        var vnCalendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        // Định dạng thời gian hiện tại theo định dạng của Việt Nam
        var createdDate = formatVnTime(vnCalendar);

        // Cộng thêm khoảng thời gian hết hạn vào thời gian hiện tại (đơn vị phút)
        vnCalendar.add(Calendar.MINUTE, paymentTimeout);

        // Định dạng thời gian hết hạn theo định dạng của Việt Nam
        var expiredDate = formatVnTime(vnCalendar);

        // Lấy địa chỉ IP từ request
        var ipAddress = request.getIpAddress();

        // Xây dựng thông tin chi tiết về đơn hàng từ request
        var orderInfo = buildPaymentDetail(request);

        // Lấy requestId từ request
        var requestId = request.getRequestId();

        // Tạo một Map để chứa các tham số cần gửi tới VNPay
        Map<String, String> params = new HashMap<>();

        // Thiết lập các tham số yêu cầu thanh toán
        params.put(VNPayParams.VERSION, VERSION); // Phiên bản API của VNPay
        params.put(VNPayParams.COMMAND, COMMAND); // Lệnh giao dịch, ví dụ "pay"
        params.put(VNPayParams.TMN_CODE, tmnCode); // Mã thương nhân từ hệ thống VNPay
        params.put(VNPayParams.AMOUNT, String.valueOf(amount)); // Số tiền thanh toán
        params.put(VNPayParams.CURRENCY, Currency.VND.getValue()); // Loại tiền tệ (VND)
        params.put(VNPayParams.TXN_REF, String.valueOf(txnRef)); // Mã giao dịch
        params.put(VNPayParams.RETURN_URL, returnUrl); // URL trả về sau khi thanh toán xong
        params.put(VNPayParams.CREATED_DATE, createdDate); // Ngày tạo giao dịch
        params.put(VNPayParams.EXPIRE_DATE, expiredDate); // Ngày hết hạn giao dịch
        params.put(VNPayParams.IP_ADDRESS, ipAddress); // Địa chỉ IP của người dùng
        params.put(VNPayParams.LOCALE, Locale.VIETNAM.getCode()); // Ngôn ngữ (vi-VN)
        params.put(VNPayParams.ORDER_INFO, orderInfo); // Thông tin chi tiết về đơn hàng
        params.put(VNPayParams.ORDER_TYPE, ORDER_TYPE); // Loại đơn hàng

        // Xây dựng URL để khởi tạo giao dịch thanh toán với VNPay dựa trên các tham số trên
        var initPaymentUrl = buildInitPaymentUrl(params);

        // Ghi log để debug: hiển thị URL khởi tạo thanh toán
        log.debug("[request_id={}] Init payment url: {}", requestId, initPaymentUrl);

        // Trả về đối tượng InitPaymentResponse với URL thanh toán được tạo ra
        return InitDepositResponse.builder()
                .vnpUrl(initPaymentUrl)
                .build();
    }

    public boolean verifyIpn(Map<String, String> params) {
        var reqSecureHash = params.get(VNPayParams.SECURE_HASH);
        params.remove(VNPayParams.SECURE_HASH);
        params.remove(VNPayParams.SECURE_HASH_TYPE);
        var hashPayload = new StringBuilder();
        var fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        var itr = fieldNames.iterator();
        while (itr.hasNext()) {
            var fieldName = itr.next();
            var fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashPayload.append(fieldName);
                hashPayload.append(Symbol.EQUAL);
                hashPayload.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    hashPayload.append(Symbol.AND);
                }
            }
        }

        var secureHash = cryptoService.sign(hashPayload.toString());
        return secureHash.equals(reqSecureHash);
    }

    private String buildPaymentDetail(InitDepositRequest request) {
        return String.format("Pay for deposit %s", request.getTxnRef());
    }

    private String buildReturnUrl(String txnRef) {
        return String.format(returnUrlFormat, txnRef);
    }

    @SneakyThrows
    private String buildInitPaymentUrl(Map<String, String> params) {
        var hashPayload = new StringBuilder();
        var query = new StringBuilder();
        var fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);   // 1. Sort field names

        var itr = fieldNames.iterator();
        while (itr.hasNext()) {
            var fieldName = itr.next();
            var fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                // 2.1. Build hash data
                hashPayload.append(fieldName);
                hashPayload.append(Symbol.EQUAL);
                hashPayload.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                // 2.2. Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append(Symbol.EQUAL);
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    query.append(Symbol.AND);
                    hashPayload.append(Symbol.AND);
                }
            }
        }

        // 3. Build secureHash
        var secureHash = cryptoService.sign(hashPayload.toString());

        // 4. Finalize query
        query.append("&vnp_SecureHash=");
        query.append(secureHash);

        return initPaymentPrefixUrl + "?" + query;
    }

    public static String formatVnTime(Calendar calendar) {
        return VNPAY_DATE_FORMAT.format(calendar.getTime());
    }
}

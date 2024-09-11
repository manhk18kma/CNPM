package kma.cnpm.beapp.domain.payment.dto.response;

import kma.cnpm.beapp.domain.common.enumType.Currency;
import kma.cnpm.beapp.domain.common.enumType.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Builder
@Data
public class TransactionResponse {
    private Long transactionId;
    Long userId;

    private BigDecimal amount;

    private TransactionStatus status;

    private String ipAddress;

    private Currency currencyPay;

    private LocalDateTime createAt;

    private BigDecimal usdToVnd;

    private BigDecimal amountInUsd;

    private List<TransactionHistoryResponse> transactionHistories;

    private PaymentGatewayResponse paymentGateway;




    @Data
    @Builder
    public static class TransactionHistoryResponse {
        private TransactionStatus status;
        private LocalDateTime timestamp;
    }

    @Data
    @Builder
    public static class PaymentGatewayResponse {
        private String name;
        private String paymentGatewayAvt;
    }
}

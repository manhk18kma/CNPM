package kma.cnpm.beapp.domain.payment.dto.response;

import lombok.Getter;

import java.util.Map;
@Getter
public class VnpayResponse {
    private String vnpAmount;
    private String vnpBankCode;
    private String vnpBankTranNo;
    private String vnpCardType;
    private String vnpOrderInfo;
    private String vnpPayDate;
    private String vnpResponseCode;
    private String vnpTmnCode;
    private String vnpTransactionNo;
    private String vnpTransactionStatus;
    private String vnpTxnRef;
    private String vnpSecureHash;

    // Constructors, getters, and setters

    public VnpayResponse(Map<String, String> params) {
        this.vnpAmount = params.get("vnp_Amount");
        this.vnpBankCode = params.get("vnp_BankCode");
        this.vnpBankTranNo = params.get("vnp_BankTranNo");
        this.vnpCardType = params.get("vnp_CardType");
        this.vnpOrderInfo = params.get("vnp_OrderInfo");
        this.vnpPayDate = params.get("vnp_PayDate");
        this.vnpResponseCode = params.get("vnp_ResponseCode");
        this.vnpTmnCode = params.get("vnp_TmnCode");
        this.vnpTransactionNo = params.get("vnp_TransactionNo");
        this.vnpTransactionStatus = params.get("vnp_TransactionStatus");
        this.vnpTxnRef = params.get("vnp_TxnRef");
        this.vnpSecureHash = params.get("vnp_SecureHash");
    }

    // You can add additional methods like toString() if needed

    @Override
    public String toString() {
        return "VnpayResponse{" +
                "vnpAmount='" + vnpAmount + '\'' +
                ", vnpBankCode='" + vnpBankCode + '\'' +
                ", vnpBankTranNo='" + vnpBankTranNo + '\'' +
                ", vnpCardType='" + vnpCardType + '\'' +
                ", vnpOrderInfo='" + vnpOrderInfo + '\'' +
                ", vnpPayDate='" + vnpPayDate + '\'' +
                ", vnpResponseCode='" + vnpResponseCode + '\'' +
                ", vnpTmnCode='" + vnpTmnCode + '\'' +
                ", vnpTransactionNo='" + vnpTransactionNo + '\'' +
                ", vnpTransactionStatus='" + vnpTransactionStatus + '\'' +
                ", vnpTxnRef='" + vnpTxnRef + '\'' +
                ", vnpSecureHash='" + vnpSecureHash + '\'' +
                '}';
    }
}
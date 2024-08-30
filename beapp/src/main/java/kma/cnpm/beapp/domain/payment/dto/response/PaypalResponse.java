package kma.cnpm.beapp.domain.payment.dto.response;

public class PaypalResponse {

    private String payerId;
    private String id;
    private String state;
    private String total;
    private String currency;
    private String paymentId;

    // Getters and Setters

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public String toString() {
        return "PaypalResponse{" +
                "payerId='" + payerId + '\'' +
                ", id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", total='" + total + '\'' +
                ", currency='" + currency + '\'' +
                ", paymentId='" + paymentId + '\'' +
                '}';
    }
}

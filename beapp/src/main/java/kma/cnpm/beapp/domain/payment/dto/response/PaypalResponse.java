package kma.cnpm.beapp.domain.payment.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaypalResponse {
    private String eventType;
    private String payerId;
    private String id;
    private String state;
    private BigDecimal total;
    private String currency;
    private String paymentId;
    private String description;

    public PaypalResponse(String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(payload);

            this.eventType = root.path("event_type").asText();
            this.id = root.path("id").asText();

            JsonNode resource = root.path("resource");

            if ("PAYMENTS.PAYMENT.CREATED".equals(eventType)) {
                this.paymentId = resource.path("id").asText();
                this.state = resource.path("state").asText();

                JsonNode transactions = resource.path("transactions");
                if (transactions.isArray() && transactions.size() > 0) {
                    JsonNode amount = transactions.get(0).path("amount");
                    this.total = BigDecimal.valueOf(Double.valueOf(amount.path("total").asText()));

//                    this.total = BigDecimal.valueOf(Long.valueOf(amount.path("total").asText()));
                    this.currency = amount.path("currency").asText();

                    this.description = transactions.get(0).path("description").asText();
                }

                this.payerId = resource.path("payer").path("payer_info").path("payer_id").asText();

            } else if ("PAYMENT.SALE.COMPLETED".equals(eventType)) {
                this.paymentId = resource.path("parent_payment").asText();
                this.state = resource.path("state").asText();

                JsonNode amount = resource.path("amount");
                this.total = BigDecimal.valueOf(Double.valueOf(amount.path("total").asText()));

//                this.total = BigDecimal.valueOf(Long.valueOf(amount.path("total").asText()));
                this.currency = amount.path("currency").asText();

                // In trường hợp này, description không có, bạn có thể thêm logic tùy chọn nếu cần

                // Nếu cần lấy thông tin khác từ resource, thêm vào đây
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý ngoại lệ trong trường hợp parsing không thành công
        }
    }

    @Override
    public String toString() {
        return "PaypalResponse{" +
                "eventType='" + eventType + '\'' +
                ", payerId='" + payerId + '\'' +
                ", id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", total='" + total + '\'' +
                ", currency='" + currency + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

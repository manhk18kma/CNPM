package kma.cnpm.beapp.domain.common.enumType;

public enum TransactionType {
    VNPAY_TRANSACTION("Transaction"),
    PAYPAL_TRANSACTION("PaypalTransaction"),

    DEFAULT("Default");

    private final String type;

    TransactionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    // Optional: method to get enum from string
    public static TransactionType fromString(String type) {
        for (TransactionType transactionType : TransactionType.values()) {
            if (transactionType.type.equalsIgnoreCase(type)) {
                return transactionType;
            }
        }
        throw new IllegalArgumentException("Unknown transaction type: " + type);
    }
}

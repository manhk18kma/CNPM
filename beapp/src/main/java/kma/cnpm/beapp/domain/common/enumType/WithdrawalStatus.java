package kma.cnpm.beapp.domain.common.enumType;

public enum WithdrawalStatus {
        PENDING,
    APPROVED,
    CANCELLED,
    DEFAULT; //For sorting

    public static boolean isDefault(WithdrawalStatus status) {
        return status == DEFAULT;
    }
}

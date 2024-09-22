package kma.cnpm.beapp.domain.common.enumType;

public enum ShipmentStatus {
    AWAITING_PICKUP("Đang chờ được nhận giao"),     // Chờ nhận hàng để vận chuyển
    IN_TRANSIT("Hàng đang được giao"),              // Hàng đang trên đường vận chuyển
    DELIVERED("Hàng đã được giao thành công"),     // Hàng đã được giao thành công
    CANCELLED("Đã hủy");                           // Hàng đã bị hủy

    private final String description;

    ShipmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

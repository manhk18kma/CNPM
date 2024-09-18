package kma.cnpm.beapp.domain.common.enumType;

public enum OrderStatus {

     READY_FOR_DELIVERY("Đơn hàng sẵn sàng được giao"),  // Đơn hàng sẵn sàng để giao
     IN_TRANSIT("Đơn hàng đang được giao"),              // Đơn hàng đang trên đường giao
     DELIVERED("Đơn hàng giao thành công"),              // Đơn hàng đã được giao thành công
     CANCELED("Đơn hàng đã bị hủy");                     // Đơn hàng đã bị hủy

     private final String description;

     OrderStatus(String description) {
         this.description = description;
     }

     public String getDescription() {
         return description;
     }

}
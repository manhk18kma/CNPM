package kma.cnpm.beapp.app.api.order;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.common.enumType.OrderStatus;
import kma.cnpm.beapp.domain.order.dto.request.OrderRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderItemResponse;
import kma.cnpm.beapp.domain.order.dto.response.OrderResponse;
import kma.cnpm.beapp.domain.order.service.OrderItemService;
import kma.cnpm.beapp.domain.order.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "APIs for order management")
public class OrderController {

    OrderService orderService;
    OrderItemService orderItemService;

    @PostMapping
    public ResponseData<String> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Đơn hàng đã được tạo thành công",
                LocalDateTime.now(),
                orderService.createOrder(orderRequest));
    }

    @PatchMapping("/accept/{id}")
    public ResponseData<String> acceptShipment(@PathVariable String id) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Đơn hàng đã được nhận vận chuyển thành công",
                LocalDateTime.now(),
                orderService.acceptShipment(id));
    }

    @PatchMapping("/complete/{id}")
    public ResponseData<String> completeOrder(@PathVariable String id) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Đơn hàng đã được giao thành công",
                LocalDateTime.now(),
                orderService.completeOrder(id));
    }

    @PatchMapping("/delete/{id}")
    public ResponseData<String> deleteOrder(@PathVariable String id) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Đơn hàng đã được hủy thành công",
                LocalDateTime.now(),
                orderService.deleteOrder(id));
    }

    @PatchMapping("/cancel/{id}")
    public ResponseData<String> deleteOrderByShipper(@PathVariable String id) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Đơn hàng đã được hủy thành công",
                LocalDateTime.now(),
                orderService.deleteOrderByShipper(id));
    }

    @GetMapping("/{id}")
    public ResponseData<OrderResponse> getOrderById(@PathVariable String id) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Đơn hàng đã được hiển thị thành công",
                LocalDateTime.now(),
                orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseData<List<OrderResponse>> getAllOrders() {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Tất cả đơn hàng đã được hiển thị thành công",
                LocalDateTime.now(),
                orderService.getAllOrders());
    }

    @GetMapping("/buyer")
    public ResponseData<List<OrderResponse>> getOrderByBuyerId() {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Tất cả đơn hàng của người dùng đã được hiển thị thành công",
                LocalDateTime.now(),
                orderService.getOrderByBuyerId());
    }

    @GetMapping("/status/{orderStatus}")
    public ResponseData<List<OrderResponse>> getOrderByStatus(@PathVariable OrderStatus orderStatus) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Tất cả đơn hàng đã được hiển thị theo trạng thái thành công",
                LocalDateTime.now(),
                orderService.getOrderByStatus(orderStatus));
    }

    @GetMapping("/buyer/status/{orderStatus}")
    public ResponseData<List<OrderResponse>> getOrderByBuyerAndStatus(@PathVariable OrderStatus orderStatus) {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Tất cả đơn hàng của người dùng đã được hiển thị theo trạng thái thành công",
                LocalDateTime.now(),
                orderService.getOrderByBuyerIdAndStatus(orderStatus));
    }

    @GetMapping("/items/buyer/status/delivered")
    public ResponseData<List<OrderItemResponse>> getOrderItemsByBuyerAndStatus() {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Tất cả mặt hàng của người dùng đã được hiển thị theo trạng thái thành công",
                LocalDateTime.now(),
                orderItemService.getOrderItemsByOrderStatusAndUser(OrderStatus.DELIVERED));
    }


}

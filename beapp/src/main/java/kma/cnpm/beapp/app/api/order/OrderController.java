package kma.cnpm.beapp.app.api.order;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.order.dto.request.OrderRequest;
import kma.cnpm.beapp.domain.order.dto.response.OrderResponse;
import kma.cnpm.beapp.domain.order.service.OrderService;
import kma.cnpm.beapp.domain.post.dto.request.PostRequest;
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

    @PostMapping
    public ResponseData<String> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return new ResponseData<>(HttpStatus.CREATED.value(),
                "Đơn hàng đã được tạo thành công",
                LocalDateTime.now(),
                orderService.createOrder(orderRequest));
    }

    @GetMapping
    public ResponseData<List<OrderResponse>> getAllOrders() {
        return new ResponseData<>(HttpStatus.OK.value(),
                "Tất cả đơn hàng đã được hiển thị thành công",
                LocalDateTime.now(),
                orderService.getAllOrders());
    }

}

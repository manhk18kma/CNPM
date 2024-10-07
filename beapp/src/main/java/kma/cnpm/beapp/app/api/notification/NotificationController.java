package kma.cnpm.beapp.app.api.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.dto.ResponseData;
import kma.cnpm.beapp.domain.notification.dto.response.CountNotificationResponse;
import kma.cnpm.beapp.domain.notification.dto.response.NotificationResponse;
import kma.cnpm.beapp.domain.notification.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "APIs for managing notifications")
public class NotificationController {

    final NotificationService notificationService;

    @Operation(
            summary = "Count unread notifications",
            description = "Retrieve the count of unread notifications for the logged-in user."
    )
    @GetMapping("/count")
    public ResponseData<CountNotificationResponse> countUnreadNotifications() {
        CountNotificationResponse response = notificationService.countNotification();
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Số lượng thông báo chưa đọc đã được lấy thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(
            summary = "Get notifications for user",
            description = "Returns a paginated list of notifications for the specified user."
    )
    @GetMapping
    public ResponseData<PageResponse<List<NotificationResponse>>> getNotificationsForUser() {
        PageResponse<List<NotificationResponse>> response = notificationService.getNotifications();
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Danh sách thông báo được trả về thành công",
                LocalDateTime.now(),
                response
        );
    }
}

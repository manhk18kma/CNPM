package kma.cnpm.beapp.domain.notification.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kma.cnpm.beapp.domain.common.enumType.NotificationType;
import kma.cnpm.beapp.domain.common.enumType.NotificationTypeRedirect;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private Long recipientId;

    private NotificationType type;

    private NotificationTypeRedirect typeRedirect;

    private String referenceId;

    private String content;

    private boolean isRead;

    private boolean isRemoved;

    private String imageUrl;

    private LocalDateTime createTime;


}

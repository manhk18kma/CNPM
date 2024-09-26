package kma.cnpm.beapp.domain.notification.entity;

import jakarta.persistence.*;
import kma.cnpm.beapp.domain.common.enumType.NotificationType;
import kma.cnpm.beapp.domain.common.enumType.NotificationTypeRedirect;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_notification")
public class Notification extends AbstractEntity<Long> {

    @Column(name = "recipient_id")
    private Long recipientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type" , length = 20)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type_redirect" , length = 20)
    private NotificationTypeRedirect typeRedirect;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "is_removed")
    private boolean isRemoved;

    @Column(name = "image_description")
    private String imageUrl;

}

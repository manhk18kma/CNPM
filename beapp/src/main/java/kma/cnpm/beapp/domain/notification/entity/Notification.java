package kma.cnpm.beapp.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kma.cnpm.beapp.domain.common.enumType.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "is_removed")
    private boolean isRemoved;

}

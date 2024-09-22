package kma.cnpm.beapp.domain.notification.repository;

import kma.cnpm.beapp.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification , Long> {
}

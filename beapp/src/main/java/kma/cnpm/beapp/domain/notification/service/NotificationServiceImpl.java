package kma.cnpm.beapp.domain.notification.service;

import kma.cnpm.beapp.domain.common.dto.UserDTO;
import kma.cnpm.beapp.domain.common.enumType.NotificationType;
import kma.cnpm.beapp.domain.notification.entity.Notification;
import kma.cnpm.beapp.domain.notification.repository.NotificationRepository;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationServiceImpl implements NotificationService {
    final NotificationRepository notificationRepository;
    final NotificationTemplateService templateService;
    final FirebaseService firebaseService;


    @SneakyThrows
    @Override
    public void createNotificationFollow(String followerFullName, Long followerId, Long followedId , String tokenDevice ) {
        String template = templateService.getTemplate(NotificationType.FOLLOW);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{followerFullName}", followerFullName);

        String content = populateTemplate(template, placeholders);
        Notification notification =  Notification.builder()
                .recipientId(followedId)
                .type(NotificationType.FOLLOW)
                .referenceId(followerId)
                .content(content)
                .isRead(false)
                .isRemoved(false)
                .build();
        Notification notificationSaved =  notificationRepository.save(notification);
        firebaseService.sendNotification(notificationSaved , tokenDevice);
    }

    @SneakyThrows
    @Override
    public void createNotificationUserView(String userViewFullName, Long userViewId, Long userViewedId , String tokenDevice) {
        String template = templateService.getTemplate(NotificationType.USER_VIEW);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{userViewFullName}", userViewFullName);

        String content = populateTemplate(template, placeholders);
        Notification notification =  Notification.builder()
                .recipientId(userViewedId)
                .type(NotificationType.USER_VIEW)
                .referenceId(userViewId)
                .content(content)
                .isRead(false)
                .isRemoved(false)
                .build();
        Notification notificationSaved =  notificationRepository.save(notification);
        firebaseService.sendNotification(notificationSaved , tokenDevice);
    }


    @SneakyThrows
    @Override
    public void balanceChange(Long userId, BigDecimal amount, BigDecimal balance, Long transactionId, NotificationType notificationType, boolean plusOrMinus, String tokenDevice) {
        String template = templateService.getTemplate(notificationType);

        // Tạo map để chứa các placeholder và giá trị của chúng
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{changeSymbol}", plusOrMinus ? "+" : "-");
        placeholders.put("{amount}", amount.toString());
        placeholders.put("{currentBalance}", balance.toString());

        // Thay thế placeholder trong template
        if (notificationType.equals(NotificationType.BALANCE_CHANGE)) {
            placeholders.put("{transactionId}", transactionId.toString());
        } else if (notificationType.equals(NotificationType.WITHDRAWAL_ACCEPTED)) {
            placeholders.put("{withdrawalId}", transactionId.toString());
        }

        // Tạo nội dung thông báo bằng cách thay thế placeholder trong template
        String content = populateTemplate(template, placeholders);

        // Tạo đối tượng Notification
        Notification notification = Notification.builder()
                .recipientId(userId)
                .type(notificationType)
                .referenceId(transactionId) // hoặc một giá trị khác phù hợp
                .content(content)
                .isRead(false)
                .isRemoved(false)
                .build();

        // Lưu thông báo vào cơ sở dữ liệu
        Notification notificationSaved = notificationRepository.save(notification);

        // Gửi thông báo đến thiết bị của người dùng
        firebaseService.sendNotification(notificationSaved, tokenDevice);
    }




    //helper function

    private String populateTemplate(String template, Map<String, String> placeholders) {
        String content = template;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        return content;
    }



}
package kma.cnpm.beapp.domain.notification.service;

import kma.cnpm.beapp.domain.common.enumType.NotificationType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;

@Service
public class NotificationTemplateService {

    private static final Map<NotificationType, String> templates = new HashMap<>();

    static {
        // Existing notifications
        templates.put(NotificationType.FOLLOW, "<strong>{followerFullName}</strong> đã theo dõi bạn.");
        templates.put(NotificationType.USER_VIEW, "<strong>{userViewFullName}</strong> đã xem hồ sơ của bạn.");

        // Balance change notifications
        templates.put(NotificationType.BALANCE_CHANGE,
                "Số dư của bạn đã thay đổi: <strong>{changeSymbol}{amount} VND</strong>. Số dư hiện tại: <strong>{currentBalance} VND</strong>. Mã giao dịch: <strong>{transactionId}</strong>.");
        templates.put(NotificationType.WITHDRAWAL_ACCEPTED,
                "Yêu cầu rút <strong>{amount} VND</strong> của bạn đã được chấp nhận. Số dư của bạn đã thay đổi <strong>{changeSymbol}{amount} VND</strong>. Số dư hiện tại: <strong>{currentBalance} VND</strong>. Mã yêu cầu: <strong>{withdrawalId}</strong>.");
        templates.put(NotificationType.WITHDRAWAL_REJECTED, "Yêu cầu rút tiền của bạn đã bị từ chối. Vui lòng kiểm tra lại thông tin hoặc liên hệ bộ phận hỗ trợ. Số dư hiện tại: {currentBalance} VND.");
    }

    public String getTemplate(NotificationType type) {
        return templates.get(type);
    }
}

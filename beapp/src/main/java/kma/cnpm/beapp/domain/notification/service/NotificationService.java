package kma.cnpm.beapp.domain.notification.service;

import kma.cnpm.beapp.domain.common.enumType.NotificationType;

import java.math.BigDecimal;

public interface NotificationService {
    void createNotificationFollow(String followerFullName, Long followerId, Long followedId , String tokenDevice);

    void createNotificationUserView(String userViewFullName, Long userViewId, Long userViewedId , String tokenDevice);

    void balanceChange(Long userId, BigDecimal amount , BigDecimal balance, Long transactionId, NotificationType notificationType, boolean plusOrMinus ,String tokenDevice);
}

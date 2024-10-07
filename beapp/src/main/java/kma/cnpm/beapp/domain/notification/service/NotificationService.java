package kma.cnpm.beapp.domain.notification.service;
import jakarta.persistence.PostRemove;
import kma.cnpm.beapp.domain.common.dto.PageResponse;
import kma.cnpm.beapp.domain.common.notificationDto.*;
import kma.cnpm.beapp.domain.notification.dto.response.CountNotificationResponse;
import kma.cnpm.beapp.domain.notification.dto.response.NotificationResponse;

import java.util.List;


public interface NotificationService {
    //USER DOMAIN
    void createNotificationFollow(CreateFollow createFollow);

    void removeFollow(RemoveFollow removeFollow);

    void createNotificationUserView(UserView userView);

    //PAYMENT DOMAIN

    void balanceChange(BalanceChange balanceChange);

    //ORDER DOMAIN
    void orderCreated(OrderCreated oderCreated);

    void shipmentCreated(ShipmentCreated shipmentCreated);

    void orderAcceptShip(OrderAcceptedShip orderAcceptedShip);

    void orderComplete(OrderCompleted orderCompleted);

    void orderCancelled(OrderCancelled orderCancelled);

    void orderConfirm(OrderConfirm orderConfirm); //confirm after order is shipped/completed

    //POST DOMAIN
    void postCreated(PostCreated postCreated); //notification for admins approve post
    void postApproved(PostApproved postApproved); // post is approved by admin -> notification for followers
    void likeCreated(LikeCreated likeCreated); // like
    void commentCreated(CommentCreated commentCreated); //comment

    //post like comment removed => remove notification
    void postRemoved(PostRemoved postRemoved); //call this method after remove post
    void unLiked(UnLiked unLiked);//call this method after unlike post
    void commentRemoved(CommentRemoved commentRemoved); //call this method after remove comment

    CountNotificationResponse countNotification();

    PageResponse<List<NotificationResponse>> getNotifications();
}

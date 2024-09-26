package kma.cnpm.beapp.domain.notification.service;
import jakarta.persistence.PostRemove;
import kma.cnpm.beapp.domain.common.notificationDto.*;


public interface NotificationService {
    //USER DOMAIN
    void createNotificationFollow(CreateFollow createFollow);

    void createNotificationUserView(UserView userView);

    //PAYMENT DOMAIN

    void balanceChange(BalanceChange balanceChange);

    //ORDER DOMAIN
    void orderCreated(OrderCreated oderCreated);

    void shipmentCreated(ShipmentCreated shipmentCreated);

    void orderAcceptShip(OrderAcceptedShip orderAcceptedShip);

    void orderComplete(OrderCompleted orderCompleted);

    void orderCancelled(OrderCancelled orderCancelled);

    void orderConfirm(OrderConfirm orderConfirm);

    //POST DOMAIN
    void postCreated(PostCreated postCreated);
    void likeCreated(LikeCreated likeCreated);
    void commentCreated(CommentCreated commentCreated);

    //post like comment removed => remove notification
    void postRemoved(PostRemoved postRemoved);
    void unLiked(UnLiked unLiked);
    void commentRemoved(CommentRemoved commentRemoved);
}

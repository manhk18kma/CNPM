package kma.cnpm.beapp.domain.common.enumType;

public enum NotificationType {
    //user
    FOLLOW,USER_VIEW,
    //payment
    BALANCE_INCREASE, BALANCE_DECREASE, BALANCE_CHANGE,

    WITHDRAWAL_ACCEPTED, WITHDRAWAL_REJECTED,WITHDRAWAL_CREATED,


    ORDER_CREATED_SELLER,  // Thông báo cho người bán
    ORDER_CREATED_BUYER   , // Thông báo cho người mua
    ORDER_CREATED_SHIPPER,//Thong bao cho shipper
    SHIPPER_ACCEPTED_SELLER,
    SHIPPER_ACCEPTED_BUYER,
    ORDER_COMPLETE_SELLER,
    ORDER_COMPLETE_BUYER,
    ORDER_CANCELLED_SELLER,
    ORDER_CANCELLED_BUYER,

    ORDER_CONFIRM_SELLER,
    ORDER_CONFIRM_BUYER,

     POST_APPROVE ,POST_CREATED,
    LIKE_CREATED,

    COMMENT_CREATED, OTHER_COMMENTER, OTHER_COMMENT_CREATED



}

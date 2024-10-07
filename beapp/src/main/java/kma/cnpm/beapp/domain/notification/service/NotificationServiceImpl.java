package kma.cnpm.beapp.domain.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kma.cnpm.beapp.domain.common.dto.UserDTO;
import kma.cnpm.beapp.domain.common.enumType.NotificationType;
import kma.cnpm.beapp.domain.common.enumType.NotificationTypeRedirect;
import kma.cnpm.beapp.domain.common.notificationDto.*;
import kma.cnpm.beapp.domain.notification.entity.Notification;
import kma.cnpm.beapp.domain.notification.repository.NotificationRepository;
import kma.cnpm.beapp.domain.user.service.AuthService;
import kma.cnpm.beapp.domain.user.service.UserRelationService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationServiceImpl implements NotificationService {
    final NotificationRepository notificationRepository;
    final NotificationTemplateService templateService;
    final FirebaseService firebaseService;
//    final UserRelationService userRelationService;
    final AuthService authService;

    @Override
    public void createNotificationFollow(CreateFollow createFollow) {
        String template = templateService.getTemplate(NotificationType.FOLLOW);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{followerFullName}", createFollow.getFollowerFullName());

        SaveAndSendNotificationRequest request = SaveAndSendNotificationRequest.builder()
                .template(template)
                .placeholders(placeholders)
                .type(NotificationType.FOLLOW)
                .recipientId(createFollow.getFollowedId())
                .referenceId(createFollow.getFollowerId().toString())
                .imgUrl(createFollow.getFollowerAvt())
                .typeRedirect(NotificationTypeRedirect.PROFILE)
                .build();
        saveAndSendNotification(request);
    }

    @Override
    public void removeFollow() {

    }


    @Override
    public void createNotificationUserView(UserView userView) {
        Notification notification = notificationRepository.findUserViewNotificationByReferenceId(userView.getUserViewedId());
        String template;
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{userViewFullName}", userView.getUserViewFullName());

        if (notification == null) {
            // Handle first-time view
            template = "<strong>{userViewFullName}</strong> đã xem hồ sơ của bạn.";
            String content = populateTemplate(template, placeholders);
            notification = Notification.builder()
                    .recipientId(userView.getUserViewedId())
                    .type(NotificationType.USER_VIEW)
                    .referenceId(userView.getUserViewedId().toString())
                    .content(content)
                    .typeRedirect(NotificationTypeRedirect.PRIVATE_PROFILE)
                    .isRead(false)
                    .imageUrl(userView.getUserViewAvt())
                    .isRemoved(false)
                    .build();
        } else {
            // Handle subsequent views
            template = "<strong>{userViewFullName}</strong> và <strong>{totalOtherViews}</strong> người khác đã xem hồ sơ của bạn.";
            placeholders.put("{totalOtherViews}", String.valueOf(userView.getTotalOtherViews()));
            String content = populateTemplate(template, placeholders);
            notification.setContent(content);
        }

        notificationRepository.save(notification);
    }



    @Override
    public void balanceChange(BalanceChange balanceChange) {
        String template = templateService.getTemplate(balanceChange.getNotificationType());

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{changeSymbol}", balanceChange.isPlusOrMinus() ? "+" : "-");
        placeholders.put("{amount}", balanceChange.getAmount().toString());
        placeholders.put("{currentBalance}", balanceChange.getBalance().toString());

        NotificationTypeRedirect typeRedirect = null;
        if (balanceChange.getNotificationType().equals(NotificationType.BALANCE_CHANGE)) {
            placeholders.put("{transactionId}", balanceChange.getTransactionId().toString());
            typeRedirect = NotificationTypeRedirect.TRANSACTION;
        } else if (balanceChange.getNotificationType().equals(NotificationType.WITHDRAWAL_ACCEPTED)) {
            typeRedirect = NotificationTypeRedirect.WITHDRAWAL;
            placeholders.put("{withdrawalId}", balanceChange.getTransactionId().toString());
        }


        SaveAndSendNotificationRequest request = SaveAndSendNotificationRequest.builder()
                .template(template)
                .placeholders(placeholders)
                .type(balanceChange.getNotificationType())
                .recipientId(balanceChange.getUserId())
                .referenceId(balanceChange.getTransactionId().toString())
                .imgUrl(balanceChange.getBalanceChangeImg())
                .typeRedirect(typeRedirect)
                .build();
        saveAndSendNotification(request);

    }

    ///ORDER
    @Override
    public void orderCreated(OrderCreated orderCreated) {
        // Handle buyer notification
        String templateBuyer = templateService.getTemplate(NotificationType.ORDER_CREATED_BUYER);
        Map<String, String> placeholdersBuyer = new HashMap<>();
        placeholdersBuyer.put("orderId", orderCreated.getOrderId());
        placeholdersBuyer.put("totalAmount", orderCreated.getAmount().toString());

        SaveAndSendNotificationRequest requestBuyer = SaveAndSendNotificationRequest.builder()
                .template(templateBuyer)
                .placeholders(placeholdersBuyer)
                .type(NotificationType.ORDER_CREATED_BUYER)
                .recipientId(orderCreated.getBuyerId())
                .referenceId(orderCreated.getOrderId())
                .imgUrl(orderCreated.getOrderImg())
                .typeRedirect(NotificationTypeRedirect.ORDER)
                .build();


        // Handle seller notification
        String templateSeller = templateService.getTemplate(NotificationType.ORDER_CREATED_SELLER);
        Map<String, String> placeholdersSeller = new HashMap<>();
        placeholdersSeller.put("totalAmount", orderCreated.getAmount().toString());
        placeholdersSeller.put("orderId", orderCreated.getOrderId());


        SaveAndSendNotificationRequest requestSeller = SaveAndSendNotificationRequest.builder()
                .template(templateSeller)
                .placeholders(placeholdersSeller)
                .type(NotificationType.ORDER_CREATED_SELLER)
                .recipientId(orderCreated.getSellerId())
                .referenceId(orderCreated.getOrderId())
                .imgUrl(orderCreated.getOrderImg())
                .typeRedirect(NotificationTypeRedirect.ORDER)
                .build();

        saveAndSendNotification(requestBuyer);
        saveAndSendNotification(requestSeller);


    }

    @Override
    public void shipmentCreated(ShipmentCreated shipmentCreated) {
        String template = templateService.getTemplate(NotificationType.ORDER_CREATED_SHIPPER);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("shipmentId", shipmentCreated.getShipmentId().toString());
        List<ShipperDTO> shipperDTOS = authService.getTokenDeviceByRoleName("SHIPPER");
        shipperDTOS.forEach(shipperDTO -> {

            SaveAndSendNotificationRequest request = SaveAndSendNotificationRequest.builder()
                    .template(template)
                    .placeholders(placeholders)
                    .type(NotificationType.ORDER_CREATED_SHIPPER)
                    .recipientId(shipperDTO.getId())
                    .referenceId(shipmentCreated.getShipmentId().toString())
                    .imgUrl(shipmentCreated.getShipmentImg())
                    .typeRedirect(NotificationTypeRedirect.SHIPMENT)
                    .build();
            saveAndSendNotification(request);

        });
    }

    @Override
    public void orderAcceptShip(OrderAcceptedShip orderAcceptedShip) {
        // Handle buyer notification
        String templateBuyer = templateService.getTemplate(NotificationType.SHIPPER_ACCEPTED_BUYER);
        Map<String, String> placeholdersBuyer = new HashMap<>();
        placeholdersBuyer.put("orderId", orderAcceptedShip.getOrderId().toString());

        SaveAndSendNotificationRequest requestBuyer = SaveAndSendNotificationRequest.builder()
                .template(templateBuyer)
                .placeholders(placeholdersBuyer)
                .type(NotificationType.SHIPPER_ACCEPTED_BUYER)
                .recipientId(orderAcceptedShip.getBuyerId())
                .referenceId(orderAcceptedShip.getOrderId().toString())
                .imgUrl(orderAcceptedShip.getOrderImg())
                .typeRedirect(NotificationTypeRedirect.ORDER)
                .build();



        // Handle seller notification
        String templateSeller = templateService.getTemplate(NotificationType.SHIPPER_ACCEPTED_SELLER);
        Map<String, String> placeholdersSeller = new HashMap<>();
        placeholdersSeller.put("orderId", orderAcceptedShip.getOrderId().toString());


        SaveAndSendNotificationRequest requestSeller= SaveAndSendNotificationRequest.builder()
                .template(templateSeller)
                .placeholders(placeholdersSeller)
                .type(NotificationType.SHIPPER_ACCEPTED_SELLER)
                .recipientId(orderAcceptedShip.getSellerId())
                .referenceId(orderAcceptedShip.getOrderId().toString())
                .imgUrl(orderAcceptedShip.getOrderImg())
                .typeRedirect(NotificationTypeRedirect.ORDER)
                .build();

        saveAndSendNotification(requestBuyer);
        saveAndSendNotification(requestSeller);



    }

    @Override
    public void orderComplete(OrderCompleted orderCompleted) {
        // Handle buyer notification
        String templateBuyer = templateService.getTemplate(NotificationType.ORDER_COMPLETE_BUYER);
        Map<String, String> placeholdersBuyer = new HashMap<>();
        placeholdersBuyer.put("orderId", orderCompleted.getOrderId().toString());

        SaveAndSendNotificationRequest requestBuyer= SaveAndSendNotificationRequest.builder()
                .template(templateBuyer)
                .placeholders(placeholdersBuyer)
                .type(NotificationType.ORDER_COMPLETE_BUYER)
                .recipientId(orderCompleted.getBuyerId())
                .referenceId(orderCompleted.getOrderId().toString())
                .imgUrl(orderCompleted.getOrderImg())
                .typeRedirect(NotificationTypeRedirect.ORDER)
                .build();


        // Handle seller notification
        String templateSeller = templateService.getTemplate(NotificationType.ORDER_COMPLETE_SELLER);
        Map<String, String> placeholdersSeller = new HashMap<>();
        placeholdersSeller.put("orderId", orderCompleted.getOrderId().toString());

        SaveAndSendNotificationRequest requestSeller= SaveAndSendNotificationRequest.builder()
                .template(templateSeller)
                .placeholders(placeholdersSeller)
                .type(NotificationType.ORDER_COMPLETE_SELLER)
                .recipientId(orderCompleted.getSellerId())
                .referenceId(orderCompleted.getOrderId().toString())
                .imgUrl(orderCompleted.getOrderImg())
                .typeRedirect(NotificationTypeRedirect.ORDER)
                .build();

        saveAndSendNotification(requestBuyer);
        saveAndSendNotification(requestSeller);

    }

    @Override
    public void orderCancelled(OrderCancelled orderCancelled) {
        // Handle buyer notification
        String templateBuyer = templateService.getTemplate(NotificationType.ORDER_CANCELLED_BUYER);
        Map<String, String> placeholdersBuyer = new HashMap<>();
        placeholdersBuyer.put("orderId", orderCancelled.getOrderId().toString());
        placeholdersBuyer.put("totalAmount", orderCancelled.getTotalAmount().toString());

        SaveAndSendNotificationRequest requestBuyer= SaveAndSendNotificationRequest.builder()
                .template(templateBuyer)
                .placeholders(placeholdersBuyer)
                .type(NotificationType.ORDER_CANCELLED_BUYER)
                .recipientId(orderCancelled.getBuyerId())
                .referenceId(orderCancelled.getOrderId().toString())
                .imgUrl(orderCancelled.getOrderImg())
                .typeRedirect(NotificationTypeRedirect.ORDER)
                .build();

        // Handle seller notification
        String templateSeller = templateService.getTemplate(NotificationType.ORDER_CANCELLED_SELLER);
        Map<String, String> placeholdersSeller = new HashMap<>();
        placeholdersSeller.put("orderId", orderCancelled.getOrderId().toString());

        SaveAndSendNotificationRequest requestSeller= SaveAndSendNotificationRequest.builder()
                .template(templateSeller)
                .placeholders(placeholdersSeller)
                .type(NotificationType.ORDER_CANCELLED_SELLER)
                .recipientId(orderCancelled.getSellerId())
                .referenceId(orderCancelled.getOrderId().toString())
                .imgUrl(orderCancelled.getOrderImg())
                .typeRedirect(NotificationTypeRedirect.ORDER)
                .build();
        saveAndSendNotification(requestBuyer);
        saveAndSendNotification(requestSeller);

    }

    @Override
    public void orderConfirm(OrderConfirm orderConfirm) {
        // Handle buyer notification
        String templateBuyer = templateService.getTemplate(NotificationType.ORDER_CONFIRM_BUYER);
        Map<String, String> placeholdersBuyer = new HashMap<>();
        placeholdersBuyer.put("orderId", orderConfirm.getOrderId().toString());
        placeholdersBuyer.put("totalAmount", orderConfirm.getTotalAmount().toString());

        SaveAndSendNotificationRequest requestBuyer= SaveAndSendNotificationRequest.builder()
                .template(templateBuyer)
                .placeholders(placeholdersBuyer)
                .type(NotificationType.ORDER_CONFIRM_BUYER)
                .recipientId(orderConfirm.getBuyerId())
                .referenceId(orderConfirm.getOrderId().toString())
                .imgUrl(orderConfirm.getOrderImg())
                .typeRedirect(NotificationTypeRedirect.ORDER)
                .build();

        // Handle seller notification
        String templateSeller = templateService.getTemplate(NotificationType.ORDER_CONFIRM_SELLER);
        Map<String, String> placeholdersSeller = new HashMap<>();
        placeholdersSeller.put("orderId", orderConfirm.getOrderId().toString());
        placeholdersSeller.put("totalAmount", orderConfirm.getTotalAmount().toString());

        SaveAndSendNotificationRequest requestSeller= SaveAndSendNotificationRequest.builder()
                .template(templateSeller)
                .placeholders(placeholdersSeller)
                .type(NotificationType.ORDER_CONFIRM_SELLER)
                .recipientId(orderConfirm.getSellerId())
                .referenceId(orderConfirm.getOrderId().toString())
                .imgUrl(orderConfirm.getOrderImg())
                .typeRedirect(NotificationTypeRedirect.ORDER)
                .build();
        saveAndSendNotification(requestBuyer);
        saveAndSendNotification(requestSeller);
    }


    //Post domain
    @Override
    public void postCreated(PostCreated postCreated) {
        String template = templateService.getTemplate(NotificationType.POST_CREATED);
        Map<String, String> placeholders = new HashMap<>();

        UserDTO userCreatedPost = authService.getUserInfo(postCreated.getPosterId());
        placeholders.put("followedUserFullName",userCreatedPost.getFullName() );
        placeholders.put("contentSnippet", postCreated.getContentSnippet());

        List<ShipperDTO> adminDTOS = authService.getTokenDeviceByRoleName("ADMIN");
        adminDTOS.forEach(adminDTO -> {
            SaveAndSendNotificationRequest request= SaveAndSendNotificationRequest.builder()
                    .template(template)
                    .placeholders(placeholders)
                    .type(NotificationType.POST_CREATED)
                    .recipientId(adminDTO.getId())
                    .referenceId(postCreated.getPostId().toString())
                    .imgUrl(postCreated.getPostUrlImg())
                    .typeRedirect(NotificationTypeRedirect.POST_MODERATION)
                    .build();
            saveAndSendNotification(request);

        });

    }

    @Override
    public void postApproved(PostApproved postApproved) {
        String template = templateService.getTemplate(NotificationType.POST_APPROVE);
        Map<String, String> placeholders = new HashMap<>();

        UserDTO userCreatedPost = authService.getUserInfo(postApproved.getPosterId());
        placeholders.put("followedUserFullName",userCreatedPost.getFullName() );
        placeholders.put("contentSnippet", postApproved.getContentSnippet());

        List<UserDTO> followersDTO = authService.getFollowersOfUser(postApproved.getPosterId());
        followersDTO.forEach(followerDTO -> {
            SaveAndSendNotificationRequest request= SaveAndSendNotificationRequest.builder()
                    .template(template)
                    .placeholders(placeholders)
                    .type(NotificationType.POST_APPROVE)
                    .recipientId(followerDTO.getUserId())
                    .referenceId(postApproved.getPostId().toString())
                    .imgUrl(postApproved.getPostUrlImg())
                    .typeRedirect(NotificationTypeRedirect.POST)
                    .build();
            saveAndSendNotification(request);
        });
    }

    @SneakyThrows
    @Override
    public void likeCreated(LikeCreated likeCreated) {
        Notification notification = notificationRepository.findLikeCreatedNotificationByPostId(likeCreated.getPostId());

        Map<String, String> placeholders = new HashMap<>();
        UserDTO liker = authService.getUserInfo(likeCreated.getLikerId());
        placeholders.put("likerFullName", liker.getFullName());
        String postSnippet = likeCreated.getContentSnippet();
        placeholders.put("postSnippet", postSnippet);

        String template;

        // Kiểm tra xem đã có thông báo Like trước đó chưa
        if (notification == null) {
            // Nếu là lượt thích đầu tiên
            template = "<strong>{likerFullName}</strong> đã thích bài viết của bạn: \"{postSnippet}\".";
            String content = populateTemplate(template, placeholders);
            notification = Notification.builder()
                    .recipientId(likeCreated.getPosterId())
                    .type(NotificationType.LIKE_CREATED)
                    .referenceId(likeCreated.getPostId().toString())
                    .content(content)
                    .typeRedirect(NotificationTypeRedirect.POST)
                    .isRead(false)
                    .imageUrl(likeCreated.getPostUrlImg())
                    .isRemoved(false)
                    .build();

        } else {
            // Nếu đã có thông báo rồi, cập nhật nội dung
            int totalLikes = likeCreated.getCountLikes();
            if (totalLikes == 1) {
                notification.setRemoved(false);
                template = "<strong>{likerFullName}</strong> đã thích bài viết của bạn: \"{postSnippet}\".";
            } else {
                // Nếu có nhiều hơn 1 lượt thích, hiển thị thông báo bao gồm số người khác
                placeholders.put("totalLikes", String.valueOf(totalLikes - 1));
                template = "<strong>{likerFullName}</strong> và <strong>{totalLikes}</strong> người khác đã thích bài viết của bạn: \"{postSnippet}\".";
            }
            String content = populateTemplate(template, placeholders);

            notification.setContent(content);
            notification.setRead(false);
        }
        notificationRepository.save(notification);

        String tokenDevice = authService.getTokenDeviceByUserId(notification.getRecipientId());
        firebaseService.sendNotification(notification , tokenDevice);

    }



    @SneakyThrows
    @Override
    public void commentCreated(CommentCreated commentCreated) {
        // Notification for other poster
        Notification notification = notificationRepository.findCommentCreatedNotificationByPostId(commentCreated.getPostId());
        Map<String, String> placeholders = new HashMap<>();
        UserDTO commenter = authService.getUserInfo(commentCreated.getCommenterId());
        placeholders.put("commenterFullName", commenter.getFullName());

        String commentSnippet = commentCreated.getCommentSnippet();
        placeholders.put("commentSnippet", commentSnippet);

        String template;
        int totalOtherCommenters = commentCreated.getOtherCommentersId().size();

        // Check if posterId is in otherCommentersId list and adjust totalOtherCommenters count
        if (commentCreated.getOtherCommentersId().contains(commentCreated.getPosterId())) {
            totalOtherCommenters = totalOtherCommenters - 1;
        }

        if (notification == null) {
            // If this is the first comment notification
            template = "<strong>{commenterFullName}</strong> đã bình luận về bài viết của bạn: \"{commentSnippet}\".";
            String content = populateTemplate(template, placeholders);
            notification = Notification.builder()
                    .recipientId(commentCreated.getPosterId())
                    .type(NotificationType.COMMENT_CREATED)
                    .referenceId(commentCreated.getPostId().toString())
                    .content(content)
                    .typeRedirect(NotificationTypeRedirect.POST)
                    .isRead(false)
                    .imageUrl(commentCreated.getPostUrlImg())
                    .isRemoved(false)
                    .build();

        } else {
            // If notification already exists
            notification.setRemoved(false);
            if (totalOtherCommenters == 0) {
                template = "<strong>{commenterFullName}</strong> đã bình luận về bài viết của bạn: \"{commentSnippet}\".";
            } else {
                placeholders.put("totalComments", String.valueOf(totalOtherCommenters));
                template = "<strong>{commenterFullName}</strong> và <strong>{totalOtherCommenters}</strong> người khác đã bình luận về bài viết của bạn: \"{commentSnippet}\".";
            }

            String content = populateTemplate(template, placeholders);
            notification.setContent(content);
            notification.setRead(false);
        }
        notificationRepository.save(notification);
        String tokenDevice = authService.getTokenDeviceByUserId(notification.getRecipientId());
        firebaseService.sendNotification(notification, tokenDevice);

        //////////////////////////////////////////////////
        // Notification for other commenters
        totalOtherCommenters = commentCreated.getOtherCommentersId().size(); // Ensure this is accurate

        for (Long id : commentCreated.getOtherCommentersId()) {
            if (id.equals(commentCreated.getPosterId())) {
                continue;
            }

            Notification otherCommenterNotification = notificationRepository
                    .findCommentCreatedForOtherCommentsByRecipientIdAndPostId(id, commentCreated.getPostId())
                    .orElseGet(() -> Notification.builder()
                            .recipientId(id)
                            .referenceId(commentCreated.getPostId().toString())
                            .type(NotificationType.OTHER_COMMENTER)
                            .typeRedirect(NotificationTypeRedirect.POST)
                            .isRead(false)
                            .isRemoved(false)
                            .imageUrl(commentCreated.getPostUrlImg())
                            .build());

            String templateForOther;
            if (totalOtherCommenters == 1) {
                templateForOther = "<strong>{commenterFullName}</strong> đã bình luận về bài viết mà bạn đã bình luận: \"{commentSnippet}\".";
            } else {
                placeholders.put("totalOtherCommenters", String.valueOf(totalOtherCommenters - 1));
                templateForOther = "<strong>{commenterFullName}</strong> và <strong>{totalOtherCommenters}</strong> người khác đã bình luận về bài viết mà bạn đã bình luận: \"{commentSnippet}\".";
            }

            String content = populateTemplate(templateForOther, placeholders);
            otherCommenterNotification.setContent(content);
            otherCommenterNotification.setRead(false);
            notificationRepository.save(otherCommenterNotification);

            String tokenDeviceOther = authService.getTokenDeviceByUserId(otherCommenterNotification.getRecipientId());
            try {
                firebaseService.sendNotification(otherCommenterNotification, tokenDeviceOther);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }


    }



    @Override
    public void postRemoved(PostRemoved postRemoved) {
        //removed post notification created
        //remove like notification
        //remove comment  notification
        //remove comment notification for other commenter
        notificationRepository.markAllNotificationsAsRemovedByReferenceId(postRemoved.getPostId());
    }

    @Override
    public void unLiked(UnLiked unLiked) {
        Notification notification = notificationRepository.findLikeCreatedNotificationByPostId(unLiked.getPostId());

        Map<String, String> placeholders = new HashMap<>();
        UserDTO liker = authService.getUserInfo(unLiked.getLatestLikerId());
        placeholders.put("likerFullName", liker.getFullName());
        String postSnippet = unLiked.getContentSnippet();
        placeholders.put("postSnippet", postSnippet);

        int totalLikes = unLiked.getCountLikes();
        String template = null;
        if (totalLikes > 1) {
            placeholders.put("totalLikes", String.valueOf(totalLikes - 1));
            template = "<strong>{likerFullName}</strong> và <strong>{totalLikes}</strong> người khác đã thích bài viết của bạn: \"{postSnippet}\".";
        } else if (totalLikes == 1) {
            template = "<strong>{likerFullName}</strong> đã thích bài viết của bạn: \"{postSnippet}\".";
        } else {
            notification.setRemoved(true);
            notificationRepository.save(notification);
            return;
        }

        String content = populateTemplate(template, placeholders);
        notification.setContent(content);
        notificationRepository.save(notification);
    }

    @Override
    public void commentRemoved(CommentRemoved commentRemoved) {
        Notification notification = notificationRepository.findCommentCreatedNotificationByPostId(commentRemoved.getPostId());
        Map<String, String> placeholders = new HashMap<>();
        UserDTO lastestCommenter = authService.getUserInfo(commentRemoved.getLastCommenterId());
        placeholders.put("commenterFullName", lastestCommenter.getFullName());

        String commentSnippet = commentRemoved.getLastCommentSnippet();
        placeholders.put("commentSnippet", commentSnippet);

        String template = null;
        int totalOtherCommenters = commentRemoved.getOtherCommentersId().size();

        if (commentRemoved.getOtherCommentersId().contains(commentRemoved.getPosterId())) {
            totalOtherCommenters -= 1;
        }
        if (totalOtherCommenters > 1) {
            placeholders.put("totalComments", String.valueOf(totalOtherCommenters));
            template = "<strong>{commenterFullName}</strong> và <strong>{totalOtherCommenters}</strong> người khác đã bình luận về bài viết của bạn: \"{commentSnippet}\".";
        } else if (totalOtherCommenters == 1) {
            template = "<strong>{commenterFullName}</strong> đã bình luận về bài viết của bạn: \"{commentSnippet}\".";
        } else {
            notification.setRemoved(true);
            notificationRepository.save(notification);
        }

        if (template != null) {
            String content = populateTemplate(template, placeholders);
            notification.setContent(content);
            notificationRepository.save(notification);
        }

        totalOtherCommenters = commentRemoved.getOtherCommentersId().size();
        for (Long id : commentRemoved.getOtherCommentersId()) {
            if (id.equals(commentRemoved.getPosterId())) {
                continue;
            }

            // Fetch notification for other commenters, handle if not found
            Notification otherCommenterNotification = notificationRepository
                    .findCommentCreatedForOtherCommentsByRecipientIdAndPostId(id, commentRemoved.getPostId())
                    .orElse(null); // Handle notification not found

            if (otherCommenterNotification != null) {
                String templateForOther;
                if (totalOtherCommenters == 1) {
                    templateForOther = "<strong>{commenterFullName}</strong> đã bình luận về bài viết mà bạn đã bình luận: \"{commentSnippet}\".";
                } else if (totalOtherCommenters > 1) {
                    placeholders.put("totalOtherCommenters", String.valueOf(totalOtherCommenters - 1));
                    templateForOther = "<strong>{commenterFullName}</strong> và <strong>{totalOtherCommenters}</strong> người khác đã bình luận về bài viết mà bạn đã bình luận: \"{commentSnippet}\".";
                } else {
                    otherCommenterNotification.setRemoved(true);
                    notificationRepository.save(otherCommenterNotification);
                    continue;
                }

                String content1 = populateTemplate(templateForOther, placeholders);
                otherCommenterNotification.setContent(content1);
                otherCommenterNotification.setRead(false);
                notificationRepository.save(otherCommenterNotification);
            }
        }
    }


    //helper function

    @SneakyThrows
    private void saveAndSendNotification(SaveAndSendNotificationRequest request){
        String content = populateTemplate(request.getTemplate(), request.getPlaceholders());
        Notification notification =   Notification.builder()
                .recipientId(request.recipientId)
                .type(request.type)
                .referenceId(request.referenceId)
                .content(content)
                .typeRedirect(request.typeRedirect)
                .isRead(false)
                .imageUrl(request.imgUrl)
                .isRemoved(false)
                .build();
        notificationRepository.save(notification);

        String tokenDevice = authService.getTokenDeviceByUserId(notification.getRecipientId());
        firebaseService.sendNotification(notification, tokenDevice);
    }

    private String populateTemplate(String template, Map<String, String> placeholders) {
        String content = template;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        return content;
    }


    @Data
    @Builder
    private static class SaveAndSendNotificationRequest{
        String template;
        Map<String,String> placeholders;
        NotificationType type;
        Long recipientId;
        String referenceId;
        String imgUrl;
        NotificationTypeRedirect typeRedirect;
    }




}
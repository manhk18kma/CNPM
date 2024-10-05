package kma.cnpm.beapp.domain.notification.repository;

import kma.cnpm.beapp.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification , Long> {
    @Query("SELECT n FROM Notification n WHERE n.referenceId = :postId AND n.type = 'LIKE_CREATED'")
    Notification findLikeCreatedNotificationByPostId(@Param("postId") Long postId);

    @Query("SELECT n FROM Notification n WHERE n.referenceId = :postId AND n.type = 'COMMENT_CREATED'")
    Notification findCommentCreatedNotificationByPostId(Long postId);


    @Query("SELECT n FROM Notification n WHERE n.recipientId = :recipientId AND n.referenceId = :postId AND n.type = 'OTHER_COMMENTER'")
    Optional<Notification> findCommentCreatedForOtherCommentsByRecipientIdAndPostId(@Param("recipientId") Long recipientId, @Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRemoved = true WHERE n.referenceId = :postId")
    void markAllNotificationsAsRemovedByReferenceId(@Param("postId") Long postId);

    @Query("SELECT n FROM Notification n WHERE n.referenceId = :recipientId and n.type = 'USER_VIEW'")
    Notification findUserViewNotificationByReferenceId(@Param("recipientId") Long recipientId);

}

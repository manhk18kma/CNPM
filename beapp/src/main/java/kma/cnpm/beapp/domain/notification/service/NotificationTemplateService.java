package kma.cnpm.beapp.domain.notification.service;

import kma.cnpm.beapp.domain.common.enumType.NotificationType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationTemplateService {

    private static final Map<NotificationType, String> templates = new HashMap<>();

    static {
        // Existing notifications
        templates.put(NotificationType.FOLLOW, "<strong>{followerFullName}</strong> đã theo dõi bạn.");

        // Balance change notifications
        templates.put(NotificationType.BALANCE_CHANGE,
                "Số dư của bạn đã thay đổi: <strong>{changeSymbol}{amount} VND</strong>. Số dư hiện tại: <strong>{currentBalance} VND</strong>. Mã giao dịch: <strong>{transactionId}</strong>.");
        templates.put(NotificationType.WITHDRAWAL_ACCEPTED,
                "Yêu cầu rút <strong>{amount} VND</strong> của bạn đã được chấp nhận. Số dư của bạn đã thay đổi <strong>{changeSymbol}{amount} VND</strong>. Số dư hiện tại: <strong>{currentBalance} VND</strong>. Mã yêu cầu: <strong>{withdrawalId}</strong>.");
        templates.put(NotificationType.WITHDRAWAL_REJECTED, "Yêu cầu rút tiền của bạn đã bị từ chối. Vui lòng kiểm tra lại thông tin hoặc liên hệ bộ phận hỗ trợ. Số dư hiện tại: {currentBalance} VND.");


        // Thông báo đơn hàng mới cho người bán
        templates.put(NotificationType.ORDER_CREATED_SELLER,
                "Bạn vừa nhận được một đơn hàng mới! Mã đơn hàng: <strong>{orderId}</strong>. Tổng giá trị: <strong>{totalAmount} VND</strong>. Vui lòng chuẩn bị đơn hàng để giao.");

// Thông báo đơn hàng mới cho người mua
        templates.put(NotificationType.ORDER_CREATED_BUYER,
                "Đơn hàng của bạn đã được tạo thành công và <strong>{totalAmount} VND</strong> đã tạm thời bị trừ khỏi số dư. Mã đơn hàng: <strong>{orderId}</strong>. Cảm ơn bạn đã mua sắm tại cửa hàng.");

// Thông báo đơn hàng mới cho shipper
        templates.put(NotificationType.ORDER_CREATED_SHIPPER,
                "Có một đơn hàng mới sẵn sàng để giao. Mã vận chuyển: <strong>{shipmentId}</strong>.");

// Thông báo shipper đã nhận đơn hàng cho người bán
        templates.put(NotificationType.SHIPPER_ACCEPTED_SELLER,
                "Đơn hàng của bạn đang được chuyển đến người mua! Mã đơn hàng: <strong>{orderId}</strong>. Vui lòng theo dõi trạng thái.");

// Thông báo shipper đã nhận đơn hàng cho người mua
        templates.put(NotificationType.SHIPPER_ACCEPTED_BUYER,
                "Đơn hàng của bạn đang trên đường vận chuyển. Mã đơn hàng: <strong>{orderId}</strong>. Hãy theo dõi trạng thái để nhận hàng.");


// Thông báo hoàn thành đơn hàng cho người bán
        templates.put(NotificationType.ORDER_COMPLETE_SELLER,
                "Đơn hàng mã <strong>{orderId}</strong> đã được giao thành công cho người mua. Số dư của bạn sẽ được cập nhật sau khi xác nhận không có khiếu nại trong vòng 3 ngày.");

// Thông báo hoàn thành đơn hàng cho người mua
        templates.put(NotificationType.ORDER_COMPLETE_BUYER,
                "Đơn hàng mã <strong>{orderId}</strong> của bạn đã được giao thành công. Nếu không có khiếu nại, tiền sẽ được chuyển cho người bán sau 3 ngày. Cảm ơn bạn đã mua sắm tại cửa hàng.");

// Thông báo hủy đơn hàng cho người bán
        templates.put(NotificationType.ORDER_CANCELLED_SELLER,
                "Đơn hàng mã <strong>{orderId}</strong> đã bị hủy bởi người mua. Vui lòng kiểm tra chi tiết. Số dư sẽ không bị ảnh hưởng.");

// Thông báo hủy đơn hàng cho người mua
        templates.put(NotificationType.ORDER_CANCELLED_BUYER,
                "Đơn hàng mã <strong>{orderId}</strong> của bạn đã được hủy thành công và số tiền tạm giữ <strong>{totalAmount} VND</strong> đã được hoàn trả vào tài khoản. Nếu có bất kỳ câu hỏi nào, vui lòng liên hệ bộ phận hỗ trợ.");


// Thông báo chuyển tiền sau khi đơn hàng hoàn tất
        // Thông báo chuyển tiền cho người mua
        templates.put(NotificationType.ORDER_CONFIRM_BUYER,
                "Số tiền <strong>{totalAmount} VND</strong> từ đơn hàng mã <strong>{orderId}</strong> đã được chuyển thành công từ bạn tới người bán. " +
                        "Cảm ơn bạn đã mua sắm tại cửa hàng.");

        // Thông báo chuyển tiền cho người bán
        templates.put(NotificationType.ORDER_CONFIRM_SELLER,
                "Số tiền <strong>{totalAmount} VND</strong> từ đơn hàng mã <strong>{orderId}</strong> đã được chuyển thành công cho bạn. " +
                        "Cảm ơn bạn đã sử dụng dịch vụ.");

        //Follower post somethign new
        templates.put(NotificationType.POST_CREATED,
                "<strong>{followedUserFullName}</strong> vừa đăng một bài viết mới: \"<strong>{contentSnippet}</strong>...\". Đừng bỏ lỡ cơ hội, xem ngay!");








    }

    public String getTemplate(NotificationType type) {
        return templates.get(type);
    }
}

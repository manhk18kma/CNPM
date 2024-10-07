package kma.cnpm.beapp;

import kma.cnpm.beapp.domain.common.enumType.NotificationType;
import kma.cnpm.beapp.domain.common.notificationDto.*;
import kma.cnpm.beapp.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
@RequiredArgsConstructor
public class BeappApplication implements CommandLineRunner {
	private final NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(BeappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		notificationService.createNotificationFollow(CreateFollow.builder()
//						.followedId(5L)
//						.followerFullName("Nguyen van manh")
//						.followerId((long) 6L)
//						.followerAvt("https://tse2.mm.bing.net/th?id=OIP.9vm7eDbnZS6Yy4ETUfEBAgHaGw&pid=Api&P=0&h=220")
//				.build());

//		notificationService.createNotificationUserView(UserView.builder()
//						.userViewFullName("Nguyen van manh")
//						.userViewAvt("123")
//						.userViewId(5L)
//						.userViewedId(6L)
//						.totalOtherViews(20)
//				.build());


//		notificationService.balanceChange(BalanceChange.builder()
//						.userId(5L)
//						.amount(BigDecimal.valueOf(850000L))
//						.transactionId(10L)
//						.notificationType(NotificationType.BALANCE_CHANGE)
//						.plusOrMinus(true)
//						.balance(BigDecimal.valueOf(7000000))
//				.build());

//		notificationService.orderCreated(OrderCreated.builder()
//				.orderId("13432432")
//				.amount(BigDecimal.valueOf(800000000000L))
//				.sellerId(5L)
//				.buyerId(6L)
//				.orderImg("https://tse2.mm.bing.net/th?id=OIP.6KK2jB4X3F2IKoX9dYto_wHaH_&pid=Api&P=0&h=220")
//				.build());


//		notificationService.shipmentCreated(ShipmentCreated.builder()
//				.orderId("432432432")
//				.shipmentImg(null)
//				.shipmentId(50L)
//				.build());

//		notificationService.orderAcceptShip(OrderAcceptedShip.builder()
//				.orderId("order.getId()")
//				.orderImg(null)
//				.buyerId(5l)
//				.sellerId(7l)
//				.build());

//		notificationService.orderComplete(OrderCompleted.builder()
//				.orderId("order.getId()")
//				.orderImg(null)
//				.buyerId(5l)
//				.sellerId(7l)
//				.build());

//		notificationService.orderCancelled(OrderCancelled.builder()
//				.totalAmount(BigDecimal.valueOf(8850222))
//				.orderId("order.getId(")
//				.orderImg(null)
//				.BuyerId(6l)
//				.sellerId(5l)
//				.build());
	}
}

package kma.cnpm.beapp.domain.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import kma.cnpm.beapp.domain.notification.entity.Notification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FirebaseService {
    final FirebaseMessaging firebaseMessaging;
    final ObjectMapper objectMapper;

    final  String tokenDevice = "cmGoQE0kKHZqUiL1gjBcSU:APA91bGWxEceUJBlMrHAPJROVVHnUiVIGflBQIqs-hi4bdFJ9Ig1Bee9ClRpPkryiHgHVJiGC87wFyDwZzI4nxQaB6X64wkh91l3TN0zJcZeUWo0vOewotcWWEe6DQifjGHVaH5ll8Rp";

    public void sendNotification(Notification notification , String tokenDeviceTarget) throws JsonProcessingException {

        String payloadJson = objectMapper.writeValueAsString(notification);

        // Xây dựng FCM message với payload dạng JSON
        Message message = Message.builder()
                .setToken("cmGoQE0kKHZqUiL1gjBcSU:APA91bEVfcMvuyn8m2fE5Ek0U0AWyCTa7tv4CTFy_Rnd_6PoEbpH4tpIyQjF-N0vAItttQAZMSydNM4vaVA1k3dfrzxCQfF9YjS1HAWR88N1lJilNb9n7W3rWuzFYymVJfHIeyHq8wDz")
                .putData("response", payloadJson)
                .build();
        try {
            System.out.println("send to " + message.toString());
            System.out.println(firebaseMessaging.send(message));
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            log.warn("Error while");
        }
    }
}

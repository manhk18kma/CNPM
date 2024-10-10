import { Injectable } from '@angular/core';
import { AngularFireMessaging } from '@angular/fire/compat/messaging';
import {BehaviorSubject, firstValueFrom} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FirebaseService {
  currentMessage = new BehaviorSubject(null);
  private vapidKey = 'BEo-xXC-e2SUiExE3Jn5qSxGbgI7X5dDQQMV8mTt3Vo4vzRbzcdGWHXovp_rkRlT74N-SKBAc9U7ucP083kVcd4'; // VAPID Key của bạn

  constructor(private afMessaging: AngularFireMessaging) {
    this.afMessaging.messages.subscribe(
      (message) => {
        console.log('Message received:', message);
        // @ts-ignore
        this.currentMessage.next(message); // Cập nhật thông báo mới
      }
    );
  }

  // Hàm yêu cầu quyền thông báo và lấy token
  async requestPermissionAndGetToken(): Promise<string | null> {
    try {
      // Sử dụng firstValueFrom để lấy giá trị đầu tiên từ Observable requestToken
      const token = await firstValueFrom(this.afMessaging.requestToken);
      if (token) {
        console.log('FCM Token received:', token);
        return token; // Trả về token nếu có
      } else {
        console.warn('Không có token được cấp.');
        return null;
      }
    } catch (error) {
      console.error('Lỗi khi yêu cầu quyền hoặc lấy FCM token:', error);
      return null; // Trả về null nếu gặp lỗi
    }
  }

  // Hàm lấy token
  async getToken(): Promise<string | null> {
    try {
      const token = await firstValueFrom(this.afMessaging.requestToken); // Sử dụng firstValueFrom để chuyển Observable thành Promise
      if (token) {
        console.log('FCM Token:', token);
        return token; // Trả về token nếu thành công
      } else {
        console.warn('Không có token được cấp.');
        return null;
      }
    } catch (error) {
      console.error('Lỗi khi lấy FCM token:', error);
      return null; // Trả về null nếu có lỗi
    }
  }
}

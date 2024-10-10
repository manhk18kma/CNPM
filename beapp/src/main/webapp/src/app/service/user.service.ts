import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {ToastrService} from "ngx-toastr";
import {TokenService} from "./token/token.service";
import {User} from "./dto/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseURL = "http://localhost:8080/api/v1/users";
  private baseURL1 = "http://localhost:8080/api/v1/notifications";

  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast: ToastrService) {
  }

  authenticated() {
    return !(this.tokenService.getEmailFormToken() == null)
  }

  registerUser(user: any): Observable<any> {
    return this.http.post<User>(`${this.baseURL}`, user);
  }

  activeToken(activeToken: any): Observable<any> {
    return this.http.post<User>(`${this.baseURL}/activate`, activeToken);
  }

  getPrivateProfile(): Observable<any> {
    return this.http.get(`${this.baseURL}/private-info`);
  }
  getPublicProfile(userId: number){
    const params = new HttpParams().set('userId', userId.toString());
    return this.http.get(`${this.baseURL}`,{params});
  }

  getGender(gender: any, data: any) {
    if (gender === 'FEMALE') {
      data = 'Nữ'
    } else if (gender === 'MALE') {
      data = 'Nam'
    } else {
      data = 'Khác'
    }
    return data;
  }
  addAddress(addAddressRequest:any): Observable<any>{
    return this.http.post(`${this.baseURL}/addresses`, addAddressRequest);
  }
  formatMoneyVND(amount: any): string {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
  }
  handleTime(time:any){
    const createTime = new Date(time); // Thời gian bắt đầu
    const currentTime = new Date(); // Thời gian hiện tại

    const timeDifference = currentTime.getTime() - createTime.getTime(); // Tính toán khoảng thời gian (milliseconds)

// Chuyển đổi thành các đơn vị thời gian cụ thể
    const seconds = Math.floor(timeDifference / 1000);
    const minutes = Math.floor(timeDifference / (1000 * 60));
    const hours = Math.floor(timeDifference / (1000 * 60 * 60));
    const days = Math.floor(timeDifference / (1000 * 60 * 60 * 24));

// Điều kiện để hiển thị
    if (minutes < 60) {
      return minutes + ' phút trước';
    } else if (hours < 24) {
      return hours + ' giờ trước';
    } else {
      return days + ' ngày trước';
    }
  }
  getNotify(): Observable<any>{
    return this.http.get(`${this.baseURL1}`);
  }
  getCountNotReadNotify(): Observable<any>{
    return this.http.get(`${this.baseURL1}/count`);
  }
}

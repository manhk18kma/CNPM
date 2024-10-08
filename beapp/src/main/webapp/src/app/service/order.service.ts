import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {TokenService} from "./token/token.service";
import {ToastrService} from "ngx-toastr";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private baseURL = "http://localhost:8080/api/v1/orders";

  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast: ToastrService) {
  }

  createOrder(orderRequest: any):Observable<any> {
    return this.http.post(`${this.baseURL}`,orderRequest);
  }
  getOrderUsersByStatus(orderStatus: any):Observable<any>{
    return this.http.get(`${this.baseURL}/status/${orderStatus}`);
  }
  getOrderBuyer(orderStatus: any):Observable<any>{
    return this.http.get(`${this.baseURL}/buyer/status/${orderStatus}`);
  }
  getOrderByID(id:any):Observable<any>{
    return this.http.get(`${this.baseURL}/${id}`);
  }
  acceptOrder(id: any):Observable<any>{
    return this.http.patch(`${this.baseURL}/accept/${id}`,id);
  }
  completeOrder(id: any):Observable<any>{
    return this.http.patch(`${this.baseURL}/complete/${id}`,id);
  }
  cancelOrderForUser(id: any):Observable<any>{
    return this.http.patch(`${this.baseURL}/delete/${id}`,id);
  }
  cancelOrder(id: any):Observable<any>{
    return this.http.patch(`${this.baseURL}/cancel/${id}`,id);
  }
}

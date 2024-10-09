import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {TokenService} from "./token/token.service";
import {ToastrService} from "ngx-toastr";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BankService {

  private baseURL = "http://localhost:8080/api/v1/banks";
  private baseURL1 = "http://localhost:8080/api/v1/accounts";
  private baseURL2 = "http://localhost:8080/api/v1/withdrawals";
  private baseURL3 = "http://localhost:8080/api/v1/transactions";
  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast: ToastrService) {
  }

  getListBank(): Observable<any> {
    return this.http.get(`${this.baseURL}`);
  }

  linkBank(bank: any): Observable<any> {
    return this.http.post(`${this.baseURL1}/banks`, bank);
  }
  unLinkBank(id: any): Observable<any> {
    return this.http.delete(`${this.baseURL1}/banks/${id}`, id);
  }
  withdraw(req: any): Observable<any> {
    return this.http.post(`${this.baseURL2}`, req);
  }
  getReqsWithDraw(status: any, sortBy: any): Observable<any> {
    const params = new HttpParams()
      .set('status', status || 'DEFAULT')
      .set('sortBy', sortBy || 'CREATE_DESC');

    return this.http.get(`${this.baseURL2}`, { params });
  }
  getReqsWithDrawAllUser(status: any, sortBy: any): Observable<any> {
    const params = new HttpParams()
      .set('status', status || 'DEFAULT')
      .set('sortBy', sortBy || 'CREATE_DESC');

    return this.http.get(`${this.baseURL2}/admin`, { params });
  }
  rejectWithDraw(id:any): Observable<any>{
    return this.http.delete(`${this.baseURL2}/admin/${id}`);
  }
  approveWithDraw(id:any): Observable<any>{
    return this.http.put(`${this.baseURL2}/${id}`,id);
  }
  recharge(req: any): Observable<any> {
    return this.http.post(`${this.baseURL3}/vnpay`, req);
  }
}

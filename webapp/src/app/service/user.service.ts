import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
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
  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast : ToastrService) { }
  logOut() {
    this.cookieService.delete('accessToken');
    this.router.navigate(['/login'])
  }
  authenticated() {
    return !(this.tokenService.getUsernameFormToken() == null)
  }
  loginUser(user: User): Observable<any> {
    return this.http.post<User>(`${this.baseURL}`, user);
  }
  registerUser(user: any): Observable<any> {
    return this.http.post<User>(`${this.baseURL}`, user);
  }
}

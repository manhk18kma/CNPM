import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {TokenService} from "../token/token.service";
import {ToastrService} from "ngx-toastr";
import {User} from "../dto/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseURL = "http://localhost:8080/api/v1/auth";
  constructor(private http: HttpClient,
              private router: Router,
              private cookieService: CookieService,
              private tokenService: TokenService,
              private toast: ToastrService) { }

  loginUser(user: User): Observable<any> {
    return this.http.post<User>(`${this.baseURL}/login`, user);
  }
  errorToLogout() {
    this.cookieService.delete('accessToken');
    this.router.navigate(['/login'])
    sessionStorage.removeItem('userProfile');
  }
  logOut(user: User) {
    return this.http.post<User>(`${this.baseURL}/logout`, user);
  }
}

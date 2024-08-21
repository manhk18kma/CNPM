import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {ToastrService} from "ngx-toastr";
import {TokenService} from "./token/token.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

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
}

import { Injectable } from '@angular/core';
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor(
    private cookieService: CookieService
  ) {
  }

  getRolesFormToken() {
    const accessToken = this.cookieService.get('accessToken')
    if (accessToken) {
      const tokenData = JSON.parse(atob(accessToken.split('.')[1]));
      return tokenData.roles
    } else {
      return [""]
    }
  }

  getEmailFormToken() {
    const accessToken = this.cookieService.get('accessToken')
    if (accessToken) {
      const tokenData = JSON.parse(atob(accessToken.split('.')[1]));
      return tokenData.sub
    } else {
      return null;
    }
  }
}

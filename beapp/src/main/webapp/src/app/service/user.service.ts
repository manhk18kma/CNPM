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
  getPublicProfile(id: number){
    const params = new HttpParams().set('id', id.toString());
    return this.http.get(`${this.baseURL}`,{params});
  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {TokenService} from "./service/token/token.service";
import {ToastrService} from "ngx-toastr";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private baseURL = "http://localhost:8080/api/v1/posts";

  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast: ToastrService) {
  }

  post(post: any) {
    return this.http.post(`${this.baseURL}`,post);
  }
  getPostsByUserID(userId: any): Observable<any> {
    return this.http.get(`${this.baseURL}/user/${userId}`);
  }
}

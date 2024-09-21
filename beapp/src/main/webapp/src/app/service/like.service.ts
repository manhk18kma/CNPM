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
export class LikeService {

  private baseURL = "http://localhost:8080/api/v1/likes";

  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast: ToastrService) {
  }

  like(postId: any): Observable<any> {
    return this.http.post(`${this.baseURL}/post/${postId}`, postId);
  }

  undoLike(postId: any): Observable<any> {
    return this.http.delete(`${this.baseURL}/post/${postId}`);
  }
}

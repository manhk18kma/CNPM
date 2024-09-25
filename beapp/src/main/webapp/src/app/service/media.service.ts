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
export class MediaService {

  private baseURL = "http://localhost:8080/api/v1/medias";

  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast: ToastrService) {
  }
  getMediaBySeller(sellerId: any): Observable<any> {
    return this.http.get(`${this.baseURL}/seller/${sellerId}`);
  }
}

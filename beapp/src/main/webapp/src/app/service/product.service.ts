import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CookieService} from "ngx-cookie-service";
import {TokenService} from "./token/token.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseURL = "http://localhost:8080/api/v1/products";

  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast: ToastrService) {
  }

  getProductByID(id: any): Observable<any> {
    return this.http.get(`${this.baseURL}/${id}`);
  }

}

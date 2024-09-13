import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {TokenService} from "./service/token/token.service";
import {ToastrService} from "ngx-toastr";
import {Observable} from "rxjs";
import {User} from "./service/dto/user";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private baseURL = "http://localhost:8080/api/v1/categories";

  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast: ToastrService) {
  }

  getAllCategories() {
    return this.http.get(`${this.baseURL}`);
  }

}

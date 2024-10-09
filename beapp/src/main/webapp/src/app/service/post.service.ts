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
export class PostService {
  private baseURL = "http://localhost:8080/api/v1/posts";

  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast: ToastrService) {
  }

  post(post: any):Observable<any> {
    return this.http.post(`${this.baseURL}`,post);
  }
  getPostsByUserID(userId: any): Observable<any> {
    return this.http.get(`${this.baseURL}/user/${userId}`);
  }
  getPostByID(id: any): Observable<any> {
    return this.http.get(`${this.baseURL}/${id}`);
  }
  getAllUploadedPosts(status: any): Observable<any> {
    return this.http.get(`${this.baseURL}/status/${status}`);

  }
  getPostApproved(approve: any): Observable<any> {
    return this.http.get(`${this.baseURL}/approve/${approve}`);
  }
  approvePost(id:any): Observable<any>{
    return this.http.patch(`${this.baseURL}/approve/${id}`,id);
  }
  deletePost(id:any): Observable<any>{
    return this.http.delete(`${this.baseURL}/${id}`,id);
  }
  unApprovePost(id:any): Observable<any>{
    return this.http.patch(`${this.baseURL}/unApprove/${id}`,id);
  }
}

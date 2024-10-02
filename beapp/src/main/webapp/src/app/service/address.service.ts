import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {TokenService} from "./token/token.service";
import {ToastrService} from "ngx-toastr";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private baseURL = "http://localhost:8080/api/v1/addresses";

  constructor(private http: HttpClient, private router: Router
    , private cookieService: CookieService, private tokenService: TokenService,
              private toast: ToastrService) {
  }

  getListProvince(): Observable<any> {
    return this.http.get(`${this.baseURL}/provinces`);
  }

  getListDistrictByProvince(provinceId: any): Observable<any>  {
    return this.http.get(`${this.baseURL}/provinces/${provinceId}/districts`, provinceId);
  }

  getListWardsByDistrict(provinceId: any, districtId: any): Observable<any>  {
    return this.http.get(`${this.baseURL}/provinces/${provinceId}/districts/${districtId}/wards`);
  }
  unLinkAddress(idAddress: any): Observable<any>{
    return this.http.delete(`${this.baseURL}/${idAddress}`);

  }
}

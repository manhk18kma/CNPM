import {Component} from '@angular/core';
import {UserService} from "../service/user.service";
import {User} from "../service/dto/user";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {ToastrService} from "ngx-toastr";
import {catchError, of} from "rxjs";
import {AuthService} from "../service/auth/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  user = new User();

  constructor(private authService: AuthService,
              private router: Router,
              private cookieService: CookieService,
              private toastrService: ToastrService) {
  }

  ngOnInit(): void {
    this.user.deviceToken = '1';
  }

  login() {

    this.authService.loginUser(this.user).pipe(
      catchError(error => {
        this.showFail()
        return of({error: 'Login failed, please try again later.'});
      })
    ).subscribe(res => {
      this.cookieService.set('accessToken', res.data.accessToken, {expires: 1, path: '/'});
      if (res.data.accessToken != null) {
        this.showSuccess()
        this.router.navigate(["/home"]);
      }
    });
  }

  showSuccess() {
    this.toastrService.success('Đăng nhập thành công !','',{
      timeOut: 1500,
    });
  }

  showFail() {
    this.toastrService.error('Đăng nhập thất bại','',{
      timeOut: 1500,
    });
  }
}

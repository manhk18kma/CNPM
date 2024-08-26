import {Component} from '@angular/core';
import {UserService} from "../service/user.service";
import {User} from "../service/dto/user";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {ToastrService} from "ngx-toastr";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  user = new User();

  constructor(private userService: UserService,
              private router: Router,
              private cookieService: CookieService,
              private toastrService: ToastrService) {
  }

  ngOnInit(): void {
  }

  login() {
    this.userService.loginUser(this.user).pipe(
      catchError(error => {
        this.showFail()
        return of({error: 'Login failed, please try again later.'});
      })
    ).subscribe(res => {
      if (res.token) {
        this.showSuccess()
        this.cookieService.set('accessToken', res.token, {expires: 1, path: '/'});
        this.router.navigate(["/profile"]);
      }
    });
  }

  showSuccess() {
    this.toastrService.success('Đăng nhập thành công !', 'Đăng nhập');
  }

  showFail() {
    this.toastrService.error('Đăng nhập thất bại', 'Đăng nhập');
  }
}

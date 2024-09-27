import {Component} from '@angular/core';
import {UserService} from "../service/user.service";
import {User} from "../service/dto/user";
import {Router} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {ToastrService} from "ngx-toastr";
import {catchError, of} from "rxjs";
import {AuthService} from "../service/auth/auth.service";
import {MessageService} from "primeng/api";
import {NgxSpinnerService} from "ngx-spinner";

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
              private messageService: MessageService,
              private spinner: NgxSpinnerService) {
  }

  ngOnInit(): void {
    this.user.deviceToken = '1';
  }

  login() {
    this.spinner.show().then(r =>{
      this.authService.loginUser(this.user).pipe(
        catchError(error => {
          this.showFail()
          return of({error: 'Đăng nhập thất bại'});
        })
      ).subscribe(res => {
        this.cookieService.set('accessToken', res.data.accessToken, {expires: 1, path: '/'});
        if (res.data.accessToken != null) {
          this.showSuccess();
          setTimeout(() => {
            this.router.navigate(["/home"]);
          },1000)
          this.spinner.hide()
        }
      });
    });

  }

  showSuccess() {
    this.messageService.add({severity: 'success', summary: 'Thao tác', detail: 'Đăng nhập thành công'});
  }

  showFail() {
    this.messageService.add({severity: 'error', summary: 'Thao tác', detail: 'Đăng nhập thất bại'});
  }
}

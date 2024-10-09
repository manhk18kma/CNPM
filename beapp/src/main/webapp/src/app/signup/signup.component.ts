import {Component} from '@angular/core';
import {User} from "../service/dto/user";
import {Router} from "@angular/router";
import {TokenService} from "../service/token/token.service";
import {UserService} from "../service/user.service";
import {ToastrService} from "ngx-toastr";
import {catchError, of} from "rxjs";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  // @ts-ignore
  user = new User()

  currentRole: any;

  constructor(private router: Router,
              private tokenService: TokenService,
              private userService: UserService,
              private messageService: MessageService) {

  }

  ngOnInit(): void {

  }
  onCaptchaResolved(captchaResponse: any){
    this.user.captchaToken = captchaResponse;
  }
  register() {
      this.userService.registerUser(this.user).pipe(
        catchError(error => {
          return of(this.messageService.add({severity: 'error', summary: 'Thao tác', detail: error.error.message}));
        })
      ).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
          setTimeout(() => {
            this.router.routeReuseStrategy.shouldReuseRoute = () => false;
            this.router.onSameUrlNavigation = 'reload';
            this.router.navigate([this.router.url]);
          }, 700);
        },
        error => {
          this.messageService.add({severity: 'error', summary: 'Thao tác', detail: error.error.message});
        });
  }
}

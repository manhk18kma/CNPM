import { Component } from '@angular/core';
import {User} from "../service/dto/user";
import {Router} from "@angular/router";
import {TokenService} from "../service/token/token.service";
import {UserService} from "../service/user.service";
import {ToastrService} from "ngx-toastr";
import {catchError, of} from "rxjs";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  user = new User();

  currentRole:any;

  constructor(private router: Router,
              private tokenService: TokenService,
              private userService: UserService,
              private toast : ToastrService) {
  }
  ngOnInit(): void {

  }

  register() {
    this.userService.registerUser(this.user).pipe(
      catchError(error => {
        return of({error: 'register failed, please try again later.'});
      })
    ).subscribe(res => {
      console.log(res)
      },
      error => {
        console.error(error)
      });
  }
}

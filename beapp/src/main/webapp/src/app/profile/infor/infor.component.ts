import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../service/dto/user";
import {AuthService} from "../../service/auth/auth.service";
import {UserService} from "../../service/user.service";
import {TokenService} from "../../service/token/token.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-infor',
  templateUrl: './infor.component.html',
  styleUrls: ['./infor.component.scss']
})
export class InforComponent implements OnInit {
  constructor(public authService: AuthService,
              private userService: UserService,
              private tokenService: TokenService,
              private route: ActivatedRoute,) {
  }

  userDetail: any;

  ngOnInit(): void {
    // let id = this.route.parent?.snapshot.paramMap.get('id')!;
    // @ts-ignore
    this.userDetail = JSON.parse(sessionStorage.getItem('userProfile'));
  }
}

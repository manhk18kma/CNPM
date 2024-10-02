import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../service/dto/user";
import {AuthService} from "../../service/auth/auth.service";
import {UserService} from "../../service/user.service";
import {TokenService} from "../../service/token/token.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-infor',
  templateUrl: './infor.component.html',
  styleUrls: ['./infor.component.scss']
})
export class InforComponent implements OnInit {
  constructor(public authService: AuthService,
              private userService: UserService,
              private tokenService: TokenService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  userDetail: any;

  ngOnInit(): void {
    // let id = this.route.parent?.snapshot.paramMap.get('id')!;
    let gender = ''
    // @ts-ignore
    this.userDetail = JSON.parse(sessionStorage.getItem('userProfile'));
    this.userDetail.gender = this.userService.getGender(this.userDetail.gender,gender);
    console.log(this.userDetail)
  }
  navigateBank(id: any){
    this.router.navigate([`bank/${id}`],id)
  }
  navigateAddress(){
    this.router.navigate([`address`])
  }
}

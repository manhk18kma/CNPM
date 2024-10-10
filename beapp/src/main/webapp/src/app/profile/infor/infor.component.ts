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
  currentRole: any;
  currentIDUser:any;
  idUser:any
  constructor(public authService: AuthService,
              public userService: UserService,
              private tokenService: TokenService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  userDetail: any;

  ngOnInit(): void {
    this.currentIDUser = this.tokenService.getIDUserFromToken();
    this.currentRole = this.tokenService.getRoleUserFromToken();
    this.idUser = this.route.parent?.snapshot.paramMap.get('id')!;
    let gender = ''
    // @ts-ignore
    this.userService.getPublicProfile(parseInt(this.idUser)).subscribe(res => {
      // @ts-ignore
      this.userDetail = {...this.userDetail, ...res.data};
    });
    if (this.currentRole != 'ROLE_SHIPPER' && this.currentRole != 'ROLE_ADMIN' && this.currentIDUser == this.idUser) {
      this.userService.getPrivateProfile().subscribe(res1 => {
        this.userDetail = {...this.userDetail, ...res1.data};
      })
    }
    this.userDetail.gender = this.userService.getGender(this.userDetail.gender,gender);
  }
  navigateBank(id: any){
    this.router.navigate([`bank/${id}`],id)
  }
  navigateAddress(){
    this.router.navigate([`address`])
  }
}

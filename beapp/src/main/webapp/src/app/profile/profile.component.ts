import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from "../service/auth/auth.service";
import {UserService} from "../service/user.service";
import {TokenService} from "../service/token/token.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from 'ngx-spinner';
import {Location} from "@angular/common";
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit{
  idUser: any;
  userDetail: any;
  currentRole: any;
  currentIDUser:any;
  constructor(public authService: AuthService,
              private userService: UserService,
              private tokenService: TokenService,
              private route: ActivatedRoute,
              private router: Router,
              private spinner: NgxSpinnerService,
              private location: Location) {
  }

  showSpinner() {
    this.spinner.show();
  }

  ngOnInit(): void {
    this.currentIDUser = this.tokenService.getIDUserFromToken();
    this.currentRole = this.tokenService.getRoleUserFromToken();
    this.idUser = this.route.snapshot.paramMap.get('id');
    this.userService.getPublicProfile(parseInt(this.idUser)).subscribe(res => {
      // @ts-ignore
      this.userDetail = {...this.userDetail, ...res.data};
    });
    if (this.currentRole != 'ROLE_SHIPPER' &&this.currentRole != 'ROLE_ADMIN' && this.currentIDUser == this.idUser) {
      this.userService.getPrivateProfile().subscribe(res1 => {
        this.userDetail = {...this.userDetail, ...res1.data};
      })
      setTimeout(() => {
        this.router.navigate(['infor'], {relativeTo: this.route});
      }, 700)
    }

  }


  // ngOnDestroy(): void {
  //   sessionStorage.removeItem('userProfile');
  // }
  protected readonly onabort = onabort;
}

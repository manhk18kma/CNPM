import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from "../service/auth/auth.service";
import {UserService} from "../service/user.service";
import {User} from "../service/dto/user";
import {TokenService} from "../service/token/token.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from 'ngx-spinner';
import {ToastrService} from "ngx-toastr";
import {timeout} from "rxjs";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {
  idUser: any;
  userDetail: any;

  constructor(public authService: AuthService,
              private userService: UserService,
              private tokenService: TokenService,
              private route: ActivatedRoute, private router: Router,
              private spinner: NgxSpinnerService,
              private toast: ToastrService) {
  }

  showSpinner() {
    this.spinner.show();
  }

  hideSpinner() {
    this.spinner.hide();
  }

  ngOnInit(): void {
    this.idUser = this.route.snapshot.paramMap.get('id');
    this.userService.getPublicProfile(parseInt(this.idUser)).subscribe(res => {
      // @ts-ignore
      this.userDetail = {...this.userDetail, ...res.data};
      this.saveSession();
    });
    this.userService.getPrivateProfile().subscribe(res1 => {
      this.userDetail = {...this.userDetail, ...res1.data};
      this.saveSession();
    })
    this.showSpinner();
    setTimeout(() => {
      this.router.navigate(['infor'], {relativeTo: this.route});
      this.spinner.hide();
      // this.toast.success("Hiển thị thành công",'',{
      //   timeOut: 1500,
      // })
    }, 500)
  }


  saveSession() {
    sessionStorage.setItem('userProfile', JSON.stringify(this.userDetail));

  }

  ngOnDestroy(): void {
    sessionStorage.removeItem('userProfile');
  }
}

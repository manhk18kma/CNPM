import {Component, OnInit} from '@angular/core';
import {ActiveToken} from "../service/dto/activeToken";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../service/user.service";
import {ActivatedRoute} from "@angular/router";
import {timestamp} from "rxjs";

@Component({
  selector: 'app-active',
  templateUrl: './active.component.html',
  styleUrls: ['./active.component.scss']
})
export class ActiveComponent implements OnInit {
  activeToken = new ActiveToken();
  isActivating = true;
  activationSuccess = false;

  constructor(private toastrService: ToastrService,
              private userService: UserService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    const token = this.route.snapshot.paramMap.get('token');
    this.activeToken.tokenDevice = null;
    if (token) {
      this.activeToken.token = token;
      this.activateAccount(this.activeToken);
    } else {
      this.isActivating = false;
      this.activeToken.token = null;
    }
  }

  activateAccount(token: any): void {
    this.userService.activeToken(token).subscribe(res => {
        this.toastrService.success(res.message)
    },error => {
      console.log(error)
      this.toastrService.error(error.error.message)
    })
  }
}

import {Component, OnInit} from '@angular/core';
import {ActiveToken} from "../service/dto/activeToken";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../service/user.service";
import {ActivatedRoute} from "@angular/router";
import {timestamp} from "rxjs";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-active',
  templateUrl: './active.component.html',
  styleUrls: ['./active.component.scss']
})
export class ActiveComponent implements OnInit {
  activeToken = new ActiveToken();
  isActivating = true;
  activationSuccess = false;

  constructor(private messageService: MessageService,
              private userService: UserService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    const token = this.route.snapshot.paramMap.get('token');
    this.activeToken.tokenDevice = '1';
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
      this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
    },error => {
      this.messageService.add({severity: 'error', summary: 'Thao tác', detail: 'Lỗi hệ thống'});
    })
  }
}

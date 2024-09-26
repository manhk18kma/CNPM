import { Component } from '@angular/core';
import {AuthService} from "../service/auth/auth.service";
import {UserService} from "../service/user.service";
import {TokenService} from "../service/token/token.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {BankService} from "../service/bank.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-recharge',
  templateUrl: './recharge.component.html',
  styleUrls: ['./recharge.component.scss']
})
export class RechargeComponent {
  bank: any;
  user: any;
  showContextMenu: boolean = false;
  contextMenuPosition = {x: 0, y: 0};
  selectedBank: any;
  reqRecharge: any = {};

  constructor(public authService: AuthService,
              private userService: UserService,
              private tokenService: TokenService,
              private route: ActivatedRoute,
              private router: Router,
              private spinner: NgxSpinnerService,
              private bankService: BankService,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
  }

  withdraw() {
    if (this.reqRecharge.amount == null || this.reqRecharge.amount < 10000) {
      this.messageService.add({severity: 'warn', summary: 'Thao tác', detail: 'Số tiền nạp không hợp lệ'});
    }
    if (this.reqRecharge.amount != null && this.reqRecharge.amount >= 10000) {
      this.reqRecharge.paymentGatewayId = 1;
      this.bankService.recharge(this.reqRecharge).subscribe(res => {
        if (res.status === 201) {
          window.location.href = res.data.vnpUrl;
        }
      }, error => {
        this.messageService.add({severity: 'warn', summary: 'Thao tác', detail: 'Lỗi dữ liệu'});
      })
    }
  }
  // Hàm copy giá trị
  copyToClipboard(value: string) {
    navigator.clipboard.writeText(value).then(() => {
      alert("Đã copy: " + value);
    }).catch(err => {
      console.error("Lỗi khi copy", err);
    });
  }
}

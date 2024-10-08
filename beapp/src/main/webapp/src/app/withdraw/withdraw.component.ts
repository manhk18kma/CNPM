import {Component, OnInit} from '@angular/core';
import {AuthService} from "../service/auth/auth.service";
import {UserService} from "../service/user.service";
import {TokenService} from "../service/token/token.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {BankService} from "../service/bank.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-withdraw',
  templateUrl: './withdraw.component.html',
  styleUrls: ['./withdraw.component.scss']
})
export class WithdrawComponent implements OnInit {
  bank: any;
  user: any;
  showContextMenu: boolean = false;
  contextMenuPosition = {x: 0, y: 0};
  selectedBank: any;
  reqWithdraw: any = {};

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
    this.userService.getPrivateProfile().subscribe(res => {
      this.user = res.data
      this.bank = res.data.bankResponses[0];
      console.log(this.bank)
    })
  }

  withdraw() {
    if (this.reqWithdraw.amount > this.user?.balance || this.reqWithdraw.amount == null || this.reqWithdraw.amount < 10000) {
      this.messageService.add({severity: 'warn', summary: 'Thao tác', detail: 'Số tiền rút không hợp lệ'});
    }
    if (this.bank?.accountHasBankId == null) {
      this.messageService.add({severity: 'warn', summary: 'Thao tác', detail: 'Vui lòng liên kết ngân hàng trước'});
    }
    if (this.bank?.accountHasBankId != null &&
      this.reqWithdraw.amount != null &&
      this.reqWithdraw.amount < this.user.balance) {
      this.reqWithdraw.accountHasBankId = this.bank.accountHasBankId;
      this.bankService.withdraw(this.reqWithdraw).subscribe(res => {
        this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
      }, error => {
        this.messageService.add({severity: 'warn', summary: 'Thao tác', detail: error.error.message});
      })
    }
  }
}

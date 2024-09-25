import {Component, OnInit} from '@angular/core';
import {BankService} from "../service/bank.service";
import {MessageService} from "primeng/api";
import {UserService} from "../service/user.service";
import {Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss']
})
export class AddComponent implements OnInit {
  banks: any;
  selectedRows: any[] = [];
  bank: any = {};
  linkedBank = false;

  constructor(private bankService: BankService,
              private messageService: MessageService,
              private userService: UserService,
              private router: Router,
              private spinner: NgxSpinnerService) {
  }

  ngOnInit(): void {
    let bank = [];
    this.userService.getPrivateProfile().subscribe(res => {
      bank = res.data.bankResponses;
      if (bank.length > 0) {
        this.linkedBank = true;
      }
    })
    this.bankService.getListBank().subscribe(res => {
      this.banks = res.data;
    })
  }

  isSelected(bank: any): boolean {
    return this.selectedRows.length > 0 && this.selectedRows[0] === bank;
  }

  selectRow(bank: any, event: MouseEvent): void {
    event.preventDefault();
    if (!Array.isArray(this.selectedRows)) {
      this.selectedRows = [];
    }
    this.selectedRows = [bank];
  }

  linkBankAccount() {
    if (this.bank.accountNumber == null) {
      this.messageService.add({severity: 'warn', summary: 'Thao tác', detail: 'Vui lòng nhập số tài khoản'});
    }
    if (this.selectedRows.length > 0) {
      this.bank.bankId = this.selectedRows[0].bankId
      this.bankService.linkBank(this.bank).subscribe(res => {
        this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
        setTimeout(() => {
          this.router.routeReuseStrategy.shouldReuseRoute = () => false;
          this.router.onSameUrlNavigation = 'reload';
          this.router.navigate([this.router.url]);
        }, 700);
      })
    } else {
      this.messageService.add({severity: 'warn', summary: 'Thao tác', detail: 'Vui lòng chọn ngân hàng'});
    }

  }
}

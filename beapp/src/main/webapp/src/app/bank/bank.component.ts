import {Component, HostListener, OnInit} from '@angular/core';
import {AuthService} from "../service/auth/auth.service";
import {UserService} from "../service/user.service";
import {TokenService} from "../service/token/token.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {ToastrService} from "ngx-toastr";
import {BankService} from "../service/bank.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-bank',
  templateUrl: './bank.component.html',
  styleUrls: ['./bank.component.scss']
})
export class BankComponent implements OnInit {
  user: any;
  showContextMenu: boolean = false;
  contextMenuPosition = {x: 0, y: 0};
  selectedBank: any;

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
      this.user = res.data;
      console.log(this.user)
    })
  }

  navigateAddBank() {
    this.router.navigate([`add-bank/${this.user.userId}`], this.user.idUser)
  }

  navigateRerchage() {
    this.router.navigate([`recharge/${this.user.userId}`], this.user.idUser)
  }
  navigateWithraw() {
    this.router.navigate([`withdraw/${this.user.userId}`], this.user.idUser)
  }
  onRightClick(event: MouseEvent, bank: any) {
    event.preventDefault(); // Prevent the default context menu
    this.showContextMenu = true;
    this.contextMenuPosition = {x: event.clientX, y: event.clientY};
    this.selectedBank = bank;
  }

  // Handle delete bank
  deleteBank(id: any) {
    this.bankService.unLinkBank(id).subscribe(res => {
      this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
      setTimeout(() => {
        this.router.routeReuseStrategy.shouldReuseRoute = () => false;
        this.router.onSameUrlNavigation = 'reload';
        this.router.navigate([this.router.url]);
      }, 500);
    }, error => {
      this.messageService.add({severity: 'error', summary: 'Thao tác', detail: error.error.message});
    })
    this.showContextMenu = false; // Hide context menu after action
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    this.showContextMenu = false;
  }
}

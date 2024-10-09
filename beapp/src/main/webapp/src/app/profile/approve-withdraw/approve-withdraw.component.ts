import { Component } from '@angular/core';
import {OrderService} from "../../service/order.service";
import {ProductService} from "../../service/product.service";
import {UserService} from "../../service/user.service";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";
import {BankService} from "../../service/bank.service";

@Component({
  selector: 'app-approve-withdraw',
  templateUrl: './approve-withdraw.component.html',
  styleUrls: ['./approve-withdraw.component.scss']
})
export class ApproveWithdrawComponent {
  syslog: any;
  syslogs: any = [];
  itemsPerPage: any
  page?: number | any;
  dataShow: any = [];
  selectedRows: any[] = [];
  selectedRow: any;
  ordersPending: any  = [];
  data: any;
  constructor(private orderService: OrderService,
              private productService: ProductService,
              public userService: UserService,
              private messageService: MessageService,
              private router: Router,
              private bankService: BankService) {
  }
  ngOnInit(): void {
    this.page = 1
    this.itemsPerPage = 10;
    this.bankService.getReqsWithDrawAllUser('DEFAULT','CREATE_DESC').subscribe(res => {
      if(res.status == 200){
        this.dataShow = res.data.items
      }
    })

  }
  selectRow(object: any, evt: any, objects: any[]) {
    this.selectedRows = [];
    this.selectedRows.push(object);
    this.selectedRow = object;
  }
  loadPage(page:any){
  }
  approve(id:any){
    this.bankService.approveWithDraw(id).subscribe(res => {
      this.messageService.add({ severity: 'success', summary: 'Thao tác', detail: res.message });
      setTimeout(()=>{
        this.router.routeReuseStrategy.shouldReuseRoute = () => false;
        this.router.onSameUrlNavigation = 'reload';
        this.router.navigate([this.router.url]);
      },500)
    },error => {
      this.messageService.add({ severity: 'success', summary: 'Thao tác', detail: 'Lỗi hệ thống' });
    })
  }
  reject(id:any){
    this.bankService.rejectWithDraw(id).subscribe(res => {
      this.messageService.add({ severity: 'success', summary: 'Thao tác', detail: res.message });
      setTimeout(()=>{
        this.router.routeReuseStrategy.shouldReuseRoute = () => false;
        this.router.onSameUrlNavigation = 'reload';
        this.router.navigate([this.router.url]);
      },500)
    },error => {
      this.messageService.add({ severity: 'success', summary: 'Thao tác', detail: 'Lỗi hệ thống' });
    })
  }
}

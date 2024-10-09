import { Component } from '@angular/core';
import {OrderService} from "../../service/order.service";
import {ProductService} from "../../service/product.service";
import {UserService} from "../../service/user.service";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";

@Component({
  selector: 'app-waiting',
  templateUrl: './waiting.component.html',
  styleUrls: ['./waiting.component.scss']
})
export class WaitingComponent {
  itemsPerPage: any
  page?: number | any;
  dataShow: any = [];
  selectedRows: any[] = [];
  selectedRow: any;
  constructor(private orderService: OrderService,
              private productService: ProductService,
              public userService: UserService,
              private messageService: MessageService,
              private orderSerice: OrderService,
              private router:Router) {
  }
  ngOnInit(): void {
    this.page = 1
    this.itemsPerPage = 10;
    this.orderService.getOrderBuyer('READY_FOR_DELIVERY').subscribe(res => {
      if(res.status == 200){
        this.dataShow = res.data
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
  cutAndJoinString(inputStr: string): string {
    return inputStr.split('-').map(part => part[0]).join('');
  }
  cancelOrder(id:any){
    this.orderService.cancelOrderForUser(id).subscribe(res => {
      this.messageService.add({ severity: 'success', summary: 'Thao tác', detail: res.message });
      setTimeout(()=>{
        this.router.navigate(['order/cancel']);
      },700)
    },error => {
      this.messageService.add({ severity: 'error', summary: 'Thao tác', detail: 'Lỗi hệ thống' });
    })
  }
}

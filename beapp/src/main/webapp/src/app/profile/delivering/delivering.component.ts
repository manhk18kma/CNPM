import { Component } from '@angular/core';
import {OrderService} from "../../service/order.service";
import {ProductService} from "../../service/product.service";
import {UserService} from "../../service/user.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-delivering',
  templateUrl: './delivering.component.html',
  styleUrls: ['./delivering.component.scss']
})
export class DeliveringComponent {
  syslog: any;
  syslogs: any = [];
  itemsPerPage: any
  page?: number | any;
  dataShow: any = [];
  selectedRows: any[] = [];
  selectedRow: any;
  ordersPending: any  = [];
  constructor(private orderService: OrderService,
              private productService: ProductService,
              private userService: UserService,
              private messageService: MessageService) {
  }
  ngOnInit(): void {
    this.page = 1
    this.itemsPerPage = 10;
    this.orderService.getOrderUsersByStatus('IN_TRANSIT').subscribe(res => {
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
  completeOrder(id: any){
    this.orderService.completeOrder(id).subscribe(res => {
      this.messageService.add({ severity: 'success', summary: 'Thao tác', detail: res.message });
    },error => {
      this.messageService.add({ severity: 'success', summary: 'Thao tác', detail: 'Lỗi hệ thống' });
    })
  }
}

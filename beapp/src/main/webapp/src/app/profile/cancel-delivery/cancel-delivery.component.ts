import { Component } from '@angular/core';
import {OrderService} from "../../service/order.service";
import {ProductService} from "../../service/product.service";
import {UserService} from "../../service/user.service";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";

@Component({
  selector: 'app-cancel-delivery',
  templateUrl: './cancel-delivery.component.html',
  styleUrls: ['./cancel-delivery.component.scss']
})
export class CancelDeliveryComponent {
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
              private messageService: MessageService,
              private router: Router) {
  }
  ngOnInit(): void {
    this.page = 1
    this.itemsPerPage = 10;
    this.orderService.getOrderUsersByStatus('CANCELED').subscribe(res => {
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
  navigateDetailOrder(id:any){
    this.router.navigate([`detail-order/${id}`])
  }
}

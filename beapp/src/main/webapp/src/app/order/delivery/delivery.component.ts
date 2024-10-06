import { Component } from '@angular/core';
import {OrderService} from "../../service/order.service";
import {ProductService} from "../../service/product.service";
import {UserService} from "../../service/user.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-delivery',
  templateUrl: './delivery.component.html',
  styleUrls: ['./delivery.component.scss']
})
export class DeliveryComponent {
  itemsPerPage: any
  page?: number | any;
  dataShow: any = [];
  selectedRows: any[] = [];
  selectedRow: any;
  constructor(private orderService: OrderService,
              private productService: ProductService,
              public userService: UserService,
              private messageService: MessageService) {
  }
  ngOnInit(): void {
    this.page = 1
    this.itemsPerPage = 10;
    this.orderService.getOrderBuyer('IN_TRANSIT').subscribe(res => {
      if(res.status == 200){
        this.dataShow = res.data
        console.log(this.dataShow)
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
}

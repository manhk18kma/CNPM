import {Component, OnInit} from '@angular/core';
import {OrderService} from "../../service/order.service";
import {ProductService} from "../../service/product.service";
import {UserService} from "../../service/user.service";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";
import {BankService} from "../../service/bank.service";

@Component({
  selector: 'app-activity',
  templateUrl: './activity.component.html',
  styleUrls: ['./activity.component.scss']
})
export class ActivityComponent implements OnInit{
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
    this.bankService.getReqsWithDraw('DEFAULT','CREATE_DESC').subscribe(res => {
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

}

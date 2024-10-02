import { Component } from '@angular/core';
import {OrderService} from "../../service/order.service";

@Component({
  selector: 'app-pending',
  templateUrl: './pending.component.html',
  styleUrls: ['./pending.component.scss']
})
export class PendingComponent {
  syslog: any;
  syslogs: any = [];
  itemsPerPage: any
  page?: number | any;
  dataShow: any = [];
  selectedRows: any[] = [];
  selectedRow: any;
  ordersPending: any  = [];
  constructor(private orderService: OrderService,
              private ) {
  }
  ngOnInit(): void {
    this.page = 1
    this.itemsPerPage = 10;
    this.orderService.getOrderUsersByStatus('READY_FOR_DELIVERY').subscribe(res => {
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
}

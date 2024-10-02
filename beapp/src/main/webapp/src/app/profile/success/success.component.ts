import { Component } from '@angular/core';

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.scss']
})
export class SuccessComponent {
  syslog: any;
  syslogs: any = [];
  itemsPerPage: any
  page?: number | any;
  dataShow: any[] = [];
  selectedRows: any[] = [];
  selectedRow: any;
  ngOnInit(): void {
    this.page = 1
    this.itemsPerPage = 10;
    this.dataShow.push("Duy Khánh");
  }
  selectRow(object: any, evt: any, objects: any[]) {
    this.selectedRows = [];
    this.selectedRows.push(object);
    this.selectedRow = object;
  }
  loadPage(page:any){

  }
}

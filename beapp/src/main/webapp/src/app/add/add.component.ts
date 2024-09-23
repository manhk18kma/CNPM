import {Component, OnInit} from '@angular/core';
import {BankService} from "../service/bank.service";

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss']
})
export class AddComponent implements OnInit{
  banks: any;
  selectedRows: any[] = [];
    constructor(private bankService: BankService) {
    }

  ngOnInit(): void {
      this.bankService.getListBank().subscribe(res => {
        this.banks = res.data;
        console.log(this.banks)
      })
  }
  selectRow(object: any, evt: any) {
    this.selectedRows = [];
    this.selectedRows.push(object);
    this.selectedRows = object;
    console.log(this.selectedRows.indexOf(object))
  }
}

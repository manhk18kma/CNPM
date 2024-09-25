import {Component, OnInit} from '@angular/core';
import {MessageService} from "primeng/api";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-response-transaction',
  templateUrl: './response-transaction.component.html',
  styleUrls: ['./response-transaction.component.scss']
})
export class ResponseTransactionComponent implements OnInit{
  constructor(private messageService: MessageService,
              private spinner: NgxSpinnerService) {
  }

  ngOnInit(): void {
    this.spinner.show();
    setTimeout(()=>{
      this.messageService.add({severity: 'success', summary: 'Thao tác', detail: 'Nạp tiền thành công'});
    },500)

  }

}

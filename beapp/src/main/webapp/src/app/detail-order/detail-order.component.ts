import {Component, OnInit} from '@angular/core';
import {MessageService} from "primeng/api";
import {UserService} from "../service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {PostService} from "../service/post.service";
import {OrderService} from "../service/order.service";
import {TokenService} from "../service/token/token.service";
import {NgxSpinnerService} from "ngx-spinner";
import {catchError, of} from "rxjs";
import {ProductService} from "../service/product.service";

@Component({
  selector: 'app-detail-order',
  templateUrl: './detail-order.component.html',
  styleUrls: ['./detail-order.component.scss']
})
export class DetailOrderComponent implements OnInit{
  id: any;
  post: any = {};
  product: any;
  currentIndex = 0;
  orderItem: any;
  shipment: any;
  data: any;
  constructor(private messageService: MessageService,
              public userService: UserService,
              private route: ActivatedRoute,
              private postService: PostService,
              private orderService: OrderService,
              public tokenService: TokenService,
              public spinner: NgxSpinnerService,
              private router: Router,
              private productService: ProductService) {
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.orderService.getOrderByID(this.id).subscribe(res => {
      this.orderItem = res.data.orderItem[0];
      this.shipment = res.data.shipment;
      this.data = res.data;
      if (this.orderItem.productId != null){
        this.productService.getProductByID(this.orderItem.productId).subscribe(res => {
          this.product = res.data;
        })
      }
    })

  }

  nextImage(images: any) {
    this.currentIndex = (this.currentIndex + 1) % images.length;
  }

  prevImage(images: any) {
    this.currentIndex = (this.currentIndex - 1 + images.length) % images.length;
  }

  goToImage(index: number) {
    this.currentIndex = index;
  }
  acceptOrder(id: any){
    this.orderService.acceptOrder(id).subscribe(res => {
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
  cancelOrder(id:any){
    this.orderService.cancelOrder(id).subscribe(res => {
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
  completeOrder(id: any){
    this.orderService.completeOrder(id).subscribe(res => {
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

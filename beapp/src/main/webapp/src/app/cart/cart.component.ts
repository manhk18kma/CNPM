import {Component, OnInit} from '@angular/core';
import {MessageService} from "primeng/api";
import {UserService} from "../service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {PostService} from "../service/post.service";
import {OrderService} from "../service/order.service";
import {catchError, of} from "rxjs";
import {TokenService} from "../service/token/token.service";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  id: any;
  post: any = {};
  product: any = {};
  currentIndex = 0;
  orderItemRequests: any = {};
  orderItem: any = {};

  constructor(private messageService: MessageService,
              public userService: UserService,
              private route: ActivatedRoute,
              private postService: PostService,
              private orderService: OrderService,
              public tokenService: TokenService,
              public spinner: NgxSpinnerService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.userService.getPrivateProfile().subscribe(res => {
      this.orderItemRequests.addressId = res.data.addresses[0].addressId;
    })
    this.orderItemRequests.orderItem = [];
    this.orderItem.quantity = 1;
    this.id = this.route.snapshot.paramMap.get('id');
    this.postService.getPostByID(this.id).subscribe(res => {
      this.post = res.data;
      this.product = res.data.product;
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

  increaseQuantity(availableStock: any) {
    if (this.orderItem.quantity < availableStock) {
      this.orderItem.quantity++;
    }
  }

  decreaseQuantity() {
    if (this.orderItem.quantity > 1) {
      this.orderItem.quantity--;
    }
  }

  onQuantityChange(event: any, availableStock: any) {
    let value = event.target.value;
    if (value > availableStock) {
      this.orderItem.quantity = availableStock;
    } else if (value < 1) {
      this.orderItem.quantity = 1;
    } else {
      this.orderItem.quantity = value;
    }
  }

  payment() {
    if (this.orderItemRequests.addressId == null){
      this.messageService.add({severity: 'error', summary: 'Thao tác', detail: 'Vui lòng liên kết địa chỉ trước'});
    }
    if (this.orderItem.quantity == 0){
      this.messageService.add({severity: 'error', summary: 'Thao tác', detail: 'Số lượng phải lớn hơn 0'})
    }
    this.orderItemRequests.orderItem = [];
    this.orderItem.productId = this.product.id;
    this.orderItemRequests.orderItem.push(this.orderItem);
    if (this.orderItemRequests.addressId != null && this.orderItem.quantity != 0) {
      this.spinner.show();
      this.orderService.createOrder(this.orderItemRequests).pipe(
        catchError(err => {
          console.log(err)
          this.messageService.add({severity: 'error', summary: 'Thao tác', detail: err.error.message});
          return of(null);
        })
      ).subscribe(res => {
        if (res) {
          this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
          setTimeout(()=>{
            this.spinner.hide()
            this.router.navigate(['order/waiting'])
          },1000)

        }
      }, error => {
        this.messageService.add({severity: 'error', summary: 'Thao tác', detail: error.error.message});
      });
    }

  }
}

import { Component } from '@angular/core';
import {OrderService} from "../../service/order.service";
import {ProductService} from "../../service/product.service";
import {UserService} from "../../service/user.service";
import {MessageService} from "primeng/api";
import {ActivatedRoute, Router} from "@angular/router";
import {BankService} from "../../service/bank.service";
import {AuthService} from "../../service/auth/auth.service";
import {CategoryService} from "../../service/category.service";
import {PostService} from "../../service/post.service";
import {NgxSpinnerService} from "ngx-spinner";
import {DomSanitizer} from "@angular/platform-browser";
import {CommentsService} from "../../service/comments.service";
import {LikeService} from "../../service/like.service";
import {TokenService} from "../../service/token/token.service";
import {catchError, of, tap} from "rxjs";

@Component({
  selector: 'app-approve-post',
  templateUrl: './approve-post.component.html',
  styleUrls: ['./approve-post.component.scss']
})
export class ApprovePostComponent {
  openPost: boolean = false;
  imageFiles: File[] = [];
  images: Awaited<string>[] = [];
  post: any = {};
  addProduct: boolean = false;
  product: any = {};
  categories: any;
  posts: any;
  userDetail: any;
  id: any;
  imagePreviewUrls: any = [];
  selectedPost: any = null;
  newComment: any = {}
  comments: any;

  constructor(public authService: AuthService,
              private categoryService: CategoryService,
              private postService: PostService,
              private route: ActivatedRoute,
              private spinner: NgxSpinnerService,
              private router: Router,
              private sanitizer: DomSanitizer,
              private cmtService: CommentsService,
              private likeService: LikeService,
              private messageService: MessageService,
              public userService: UserService,
              public tokenService: TokenService
  ) {
  }

  ngOnInit(): void {
    this.post.product = null;
    this.id = this.route.parent?.snapshot.paramMap.get('id');
    this.getAllPost();
  }

  openCommentModal(post: any) {
    this.newComment.content = ''
    this.selectedPost = post;
    this.getCommentsByPost(post.id);
  }

  get lockScroll() {
    return {
      'overflow-y': this.selectedPost ? 'hidden' : 'auto',
      'position': this.selectedPost ? 'fixed' : 'relative',
      'width': '100%'
    };
  }

  getCommentsByPost(id: any) {
    this.cmtService.getCommentsByPostID(id).pipe(
      tap(res => {
        this.comments = res.data;
      }),
      catchError(error => {
        return of([]);
      })
    ).subscribe();
  }

  addComment(post: any) {
    if (this.newComment.content.trim() == '') {
      this.selectedPost = null
      this.messageService.add({severity: 'warn', summary: 'Thao tác', detail: 'Chưa nhập nội dung bình luận'});
    } else {
      this.newComment.postId = post.id;
      this.spinner.show()
      setTimeout(() => {
        this.cmtService.sendComment(this.newComment).pipe(
          tap(res => {
            this.spinner.hide()
            this.selectedPost = null
            this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
            post.commentTotal += 1;
          }),
          catchError(error => {
            this.messageService.add({severity: 'error', summary: 'Thao tác', detail: error.message});
            return of(null);
          })
        ).subscribe();
      }, 1000)
    }

  }

  getAllPost() {
    this.postService.getPostApproved('FALSE').subscribe(res => {
      this.posts = res.data
    })
  }
  approve(id: any){
    this.postService.approvePost(id).subscribe(res => {
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
  decline(id: any){
    this.postService.unApprovePost(id).subscribe(res => {
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
  viewProfile(id: any){
    this.router.navigate([`profile/${id}`])
  }
}

import { Component } from '@angular/core';
import {AuthService} from "../../service/auth/auth.service";
import {CategoryService} from "../../service/category.service";
import {PostService} from "../../service/post.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {DomSanitizer} from "@angular/platform-browser";
import {CommentsService} from "../../service/comments.service";
import {LikeService} from "../../service/like.service";
import {MessageService} from "primeng/api";
import {catchError, of, tap} from "rxjs";
import {UserService} from "../../service/user.service";
import {TokenService} from "../../service/token/token.service";

@Component({
  selector: 'app-liked',
  templateUrl: './liked.component.html',
  styleUrls: ['./liked.component.scss']
})
export class LikedComponent {
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
    this.getPostsByUserID(this.id)
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

  closeModal() {
    this.selectedPost = null;
  }
  getPostsByUserID(id: any) {
    this.likeService.getLikedPosts().subscribe(res => {
      this.posts = res.data
    })
  }

  toggleLike(id: any, post: any) {
    if (!post.liked) {
      this.likeService.like(id).subscribe(res => {
        this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
        post.liked = true;
        post.likeTotal += 1
      }, error => {
        this.messageService.add({severity: 'error', summary: 'Thao tác', detail: error.message});
      })
    } else {
      this.likeService.undoLike(id).subscribe(res => {
        this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
        post.liked = false;
        post.likeTotal -= 1
      }, error => {
        this.messageService.add({severity: 'error', summary: 'Thao tác', detail: error.message});
      })
    }
  }
  navigatePayment(id: any){
    this.router.navigate([`payment/${id}`],id);
  }
}

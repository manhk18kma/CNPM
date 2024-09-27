import { Component } from '@angular/core';
import {AuthService} from "../service/auth/auth.service";
import {CategoryService} from "../service/category.service";
import {PostService} from "../service/post.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {DomSanitizer} from "@angular/platform-browser";
import {CommentsService} from "../service/comments.service";
import {LikeService} from "../service/like.service";
import {MessageService} from "primeng/api";
import {catchError, of, tap} from "rxjs";

@Component({
  selector: 'app-detail-post',
  templateUrl: './detail-post.component.html',
  styleUrls: ['./detail-post.component.scss']
})
export class DetailPostComponent {
  openPost: boolean = false;
  imageFiles: File[] = [];
  images: Awaited<string>[] = [];
  addProduct: boolean = false;
  product: any = {};
  categories: any;
  post: any = {};
  userDetail: any;
  id: any;
  imagePreviewUrls: any = [];
  selectedPost: any = null;
  newComment: any = {}
  comments: any;
  openComment = false;
  constructor(public authService: AuthService,
              private categoryService: CategoryService,
              private postervice: PostService,
              private route: ActivatedRoute,
              private spinner: NgxSpinnerService,
              private router: Router,
              private sanitizer: DomSanitizer,
              private cmtService: CommentsService,
              private likeService: LikeService,
              private messageService: MessageService
  ) {
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    // @ts-ignore
    this.userDetail = JSON.parse(sessionStorage.getItem('userProfile'));
    console.log(this.userDetail)
    this.getPostsByPostID(this.id)
  }

  openCommentModal(post: any) {
    this.openComment = true;
    this.newComment.content = ''
    this.selectedPost = post;
    this.getCommentsByPost(post.id);

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

  addProd() {
    this.categories = {};
    this.addProduct = true;
    this.categoryService.getAllCategories().subscribe(res => {
      this.categories = res.data;
      this.product.categoryId = this.categories[0].id;
    })

  }

  cancelAddProd() {
    this.addProduct = false;
    this.categories = null;
  }

  onInput(event: Event) {
    const textarea = event.target as HTMLTextAreaElement;
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }

  getPostsByPostID(id: any) {
    this.postervice.getPostByID(id).subscribe(res => {

      this.post = res.data
      console.log(this.post)
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
}

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
import {UserService} from "../service/user.service";
import {TokenService} from "../service/token/token.service";
import {catchError, of, tap} from "rxjs";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent {
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

  // Method to handle image selection
  onFilesSelected(event: any): void {
    const files: File[] = event.target.files;

    if (files.length > 0) {
      this.images = []; // Clear previous file selection
      // this.imagePreviewUrls = []; // Clear previous previews
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        this.imageFiles.push(file); // Add to the array of selected files
        // Generate a URL to display the image preview
        const objectURL = URL.createObjectURL(file);
        this.imagePreviewUrls.push(this.sanitizer.bypassSecurityTrustUrl(objectURL) as string);
      }
    }
  }

  private convertToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        resolve(reader.result as string); // Resolve with the Base64 string
      };
      reader.onerror = reject; // Handle any error
      reader.readAsDataURL(file); // Convert file to Base64
    });
  }


  onCategoryChange(event: any) {
    this.product.categoryId = event.target.value;
  }

  async upPost() {
    if (this.addProduct == true) {
      if (this.imageFiles.length > 0) {
        this.product.imageBase64 = [];
        this.images = [];
        for (let file of this.imageFiles) {
          try {
            const base64String = await this.convertToBase64(file);
            this.images.push(base64String);
          } catch (error) {
            this.messageService.add({severity: 'error', summary: 'Error', detail: 'Failed to convert image to Base64'});
            return; // Stop the process if any image conversion fails
          }
        }
        this.product.imageBase64 = this.images;
      }
      this.post.product = this.product;
    }
    this.spinner.show()
    this.postService.post(this.post).subscribe(res => {
      setTimeout(() => {
        this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
        this.spinner.hide()
        this.openPost = false;
      }, 500)
    }, error => {
      this.messageService.add({severity: 'error', summary: 'Thao tác', detail: error.message});
    })
  }

  getAllPost() {
    this.postService.getPostApproved('TRUE').subscribe(res => {
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
  viewProfile(id: any){
      this.router.navigate([`profile/${id}`])
  }
}

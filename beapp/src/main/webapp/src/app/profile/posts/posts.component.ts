import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../service/auth/auth.service";
import {CategoryService} from "../../service/category.service";
import {PostService} from "../../service/post.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {ToastrService} from "ngx-toastr";
import {DomSanitizer} from "@angular/platform-browser";
import {CommentsService} from "../../service/comments.service";
import {catchError, of, tap} from "rxjs";
import {LikeService} from "../../service/like.service";

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})
export class PostsComponent implements OnInit {
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
              private toast: ToastrService,
              private router: Router,
              private sanitizer: DomSanitizer,
              private cmtService: CommentsService,
              private likeService: LikeService
  ) {
  }

  ngOnInit(): void {
    this.post.product = null;
    this.id = this.route.parent?.snapshot.paramMap.get('id');
    // @ts-ignore
    this.userDetail = JSON.parse(sessionStorage.getItem('userProfile'));
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
        this.toast.error(error.message);
        return of([]);
      })
    ).subscribe();
  }

  addComment(post: any) {
    if (this.newComment.content.trim() == '') {
      this.selectedPost = null
      this.toast.warning('Chưa nhập nội dung bình luận')
    } else {
      this.newComment.postId = post.id;
      this.spinner.show()
      setTimeout(() => {
        this.cmtService.sendComment(this.newComment).pipe(
          tap(res => {
            this.spinner.hide()
            this.selectedPost = null
            this.toast.success(res.message);
          }),
          catchError(error => {
            this.toast.error(error.message);
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

  upPost() {
    if (this.addProduct == true) {
      if (this.imageFiles.length > 0) {
        this.product.imageBase64 = [];
        this.images = [];
        const promises = this.imageFiles.map(file => this.convertToBase64(file));
        Promise.all(promises).then((base64Array) => {
          this.images = base64Array;
          this.product.imageBase64 = this.images;
        });
      }
      this.post.product = this.product;
    }
    this.spinner.show();
    setTimeout(() => {
      this.postService.post(this.post).subscribe(res => {
        this.toast.success(res.message, '', {timeOut: 1000})
        this.spinner.hide()
      }, error => {
        this.toast.error(error.message);
        this.spinner.hide()
      })
      this.router.navigate(['/']).then(() => {
        this.router.navigate([`/profile/${this.id}/posts`])
      })
    }, 1000)

  }

  getPostsByUserID(id: any) {
    this.postService.getPostsByUserID(parseInt(id)).subscribe(res => {
      this.posts = res.data
    })
  }
  toggleLike(id: any,post: any) {
    if (post.isLiked) {
      this.likeService.like(id).subscribe(res => {
        console.log(res);
      })
    } else {
      this.likeService.undoLike(id).subscribe(res => {
        console.log(res);
      })
    }
  }
}

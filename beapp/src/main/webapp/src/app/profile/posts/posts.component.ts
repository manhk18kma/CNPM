import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../service/auth/auth.service";
import {CategoryService} from "../../category.service";
import {PostService} from "../../post.service";
import {ActivatedRoute} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})
export class PostsComponent implements OnInit {
  openPost: boolean = false;
  images: { url: string }[] = [];
  post: any = {};
  addProduct: boolean = false;
  product: any = {};
  categories: any;
  posts: any;
  userDetail: any;
  constructor(public authService: AuthService,
              private categoryService: CategoryService,
              private postService: PostService,
              private route: ActivatedRoute,
              private spinner: NgxSpinnerService,
              private toast : ToastrService) {
  }

  ngOnInit(): void {
    this.post.product = null;
      let id = this.route.parent?.snapshot.paramMap.get('id');
      // @ts-ignore
    this.userDetail = JSON.parse(sessionStorage.getItem('userProfile'));
      this.getPostsByUserID(id)
  }
  addProd(){
    this.categories = {};
    this.addProduct = true;
    this.categoryService.getAllCategories().subscribe(res =>{
      this.categories = res.data;
      this.product.categoryId = this.categories[0].id;
    })

  }
  cancelAddProd(){
    this.addProduct = false;
    this.categories = null;
  }
  onInput(event: Event) {
    const textarea = event.target as HTMLTextAreaElement;
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }

  onFileSelected(event: any) {
    const files = event.target.files;
    this.images = [];  // Khởi tạo mảng images để lưu các ảnh mới

    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      const reader = new FileReader();

      reader.onload = (e: ProgressEvent<FileReader>) => {
        // Chuyển đổi kết quả thành base64 và lưu vào mảng images
        const base64String = (e.target as FileReader).result as string;
        this.images.push({ url: base64String });
      };
      // Đọc tệp tin dưới dạng URL base64
      reader.readAsDataURL(file);
    }
    this.images.forEach(item =>{
      this.product.imageBase64.push(item.url)
    })
  }

  dataURLtoBlob(dataurl: string) {
    // @ts-ignore
    const arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
    }
    return new Blob([u8arr], {type: mime});
  }
  onCategoryChange(event: any) {
    this.product.categoryId = event.target.value;
  }
  upPost(){
    if (this.addProduct == true) {
      this.post.product = this.product;
    }
      this.spinner.show();
      setTimeout(()=>{
        this.postService.post(this.post).subscribe(res =>{
          this.toast.success("Đăng bài thành công")
        },error => {
          this.toast.error(error.message);
        })
        this.spinner.hide()
      },2000)

  }
  getPostsByUserID(id: any){
    this.postService.getPostsByUserID(parseInt(id)).subscribe(res =>{
      this.posts = res.data
    })
  }
}

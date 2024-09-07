import {Component, OnInit} from '@angular/core';
import {AuthService} from "../service/auth/auth.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  images: { url: string }[] = [];
  textContent: string = '';
  openPost: boolean = false;
  tabPost: boolean = true;
  tabInfor: boolean = false;
  tabImage: boolean = false;
  tabHistory: boolean = false;
  constructor(public authService :AuthService) {
  }

  onFileSelected(event: any) {
    const files = event.target.files;
    for (let i = 0; i < files.length; i++) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.images.push({url: e.target.result});
      };
      reader.readAsDataURL(files[i]);
    }
  }

  onInput(event: Event) {
    const textarea = event.target as HTMLTextAreaElement;
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
  }

  sendText() {
    // Xử lý khi người dùng ấn nút "Gửi"
    console.log(this.textContent);
  }

  ngOnInit(): void {
    const textarea = document.querySelector('.auto-resize-textarea') as HTMLTextAreaElement;

    textarea.addEventListener('input', () => {
      textarea.style.height = 'auto';
      textarea.style.height = textarea.scrollHeight + 'px';
    });
  }

  directPage(text: any) {
    console.log(text)
    if (text.toLowerCase() == 'bài đăng') {
      this.tabPost = true;
      this.tabInfor = false;
      this.tabHistory = false;
      this.tabImage = false
    } else if (text.toLowerCase() == 'thông tin cá nhân') {
      this.tabPost = false;
      this.tabInfor = true;
      this.tabHistory = false;
      this.tabImage = false
    }
    else if (text.toLowerCase() == 'hình ảnh/video') {
      this.tabPost = false;
      this.tabInfor = false;
      this.tabHistory = false;
      this.tabImage = true
    }
    else if (text.toLowerCase() == 'hoạt động') {
      this.tabPost = false;
      this.tabInfor = false;
      this.tabHistory = true;
      this.tabImage = false
    }
  }
}

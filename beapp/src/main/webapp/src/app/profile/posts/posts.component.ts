import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../service/auth/auth.service";

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})
export class PostsComponent implements OnInit {
  openPost: boolean = false;
  textContent: string = '';
  images: { url: string }[] = [];
  constructor(public authService: AuthService) {
  }

  ngOnInit(): void {
    const textarea = document.querySelector('.auto-resize-textarea') as HTMLTextAreaElement;

    textarea.addEventListener('input', () => {
      textarea.style.height = 'auto';
      textarea.style.height = textarea.scrollHeight + 'px';
    });
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

}

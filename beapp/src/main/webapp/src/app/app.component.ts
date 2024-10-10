import {Component, HostListener, OnInit} from '@angular/core';
import {UserService} from "./service/user.service";
import {Router} from "@angular/router";
import {TokenService} from "./service/token/token.service";
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {AngularFireMessaging} from "@angular/fire/compat/messaging";
import {FirebaseService} from "./firebase.service";
import {mergeMap, mergeMapTo} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'webapp';
  isTransparent = false;
  clickSearch = false;
  key: any;
  isMobile: boolean = false;
  showMobileMenu: boolean = false;
  currentRole: any;
  showNotifications: boolean = false;
  notifications: any = []; // Mảng thông báo mẫu
  count = 0;
  message: any;
  currentToken: any;
  constructor(public userService: UserService,
              private router: Router,
              private tokenService: TokenService,
              private breakpointObserver: BreakpointObserver,
              private fb: FirebaseService,
              private afMessaging: AngularFireMessaging
  ) {

  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const scrollOffset = window.pageYOffset;
    this.isTransparent = scrollOffset > 50;
  }

  openInputSearch() {
    this.clickSearch = true;
  }

  closeInputSearch() {
    this.clickSearch = false;
  }

  navigateProfile() {
    const id = this.tokenService.getIDUserFromToken();
    this.router.navigate(['/profile', id]);

  }

  sendResultSearch(key: any) {
    this.clickSearch = false;
    key = key.trim();
    if (key.length > 0) {
      this.router.navigate(['/']).then(() => {
        this.router.navigate(['search', key])
      })
    }
  }

  ngOnInit(): void {
    this.breakpointObserver.observe([Breakpoints.Handset, Breakpoints.Tablet])
      .subscribe(result => {
        this.isMobile = result.matches;
      });
    this.currentRole = this.tokenService.getRoleUserFromToken();
    this.getCount();
    this.listenToMessages();
    // this.requestPermission();
    this.registerServiceWorker();
  }

  openMobileMenu() {
    this.showMobileMenu = !this.showMobileMenu;  // Toggles the mobile menu visibility
  }

  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
    if (this.showNotifications) {
      this.userService.getNotify().subscribe(res =>{
        this.notifications = res.data;
      })
      this.getCount();
    }
  }
  getCount(){
    this.userService.getCountNotReadNotify().subscribe(res =>{
      this.count = res.data.totalNotRead;
    })
  }
  registerServiceWorker() {
    if ('serviceWorker' in navigator) {
      navigator.serviceWorker.register('/firebase-messaging-sw.js')
        .then((registration) => {
          console.log('Service Worker registered with scope:', registration.scope);

          if (registration.active) {
            console.log('Service Worker is active.');
          } else {
            console.log('Service Worker is not active yet.');
          }
          // this.fb.requestPermission();
          // this.fb.receiveMessage();
          this.fb.getToken().then((token) => {
            if (token) {
              console.log('Device FCM Token:', token);
              // Xử lý token (gửi lên server, lưu trữ, v.v.)
            }
          });
          this.fb.requestPermissionAndGetToken().then((token) => {
            if (token) {
              console.log('Device FCM Token:', token);
              // Xử lý token (gửi lên server, lưu trữ, v.v.)
            }
          });
        })
        .catch((err) => {
          console.error('Service Worker registration failed:', err);
          // Xử lý các lỗi khác nhau ở đây, có thể thông báo tới người dùng
        });
    } else {
      console.warn('Service Worker is not supported by this browser.');
    }
  }
  requestPermission() {
    // Kiểm tra nếu có quyền thông báo
    this.afMessaging.requestPermission
      .pipe(
        // @ts-ignore
        mergeMap(() => this.afMessaging.getToken({ vapidKey: 'BEo-xXC-e2SUiExE3Jn5qSxGbgI7X5dDQQMV8mTt3Vo4vzRbzcdGWHXovp_rkRlT74N-SKBAc9U7ucP083kVcd4' })) // Thay bằng VAPID Key của bạn
      )
      .subscribe(
        (token) => {
          if (token) {
            console.log('FCM Token:', token);
            this.currentToken = token;
            // Gửi token lên server BE của bạn tại đây (nếu cần)
          } else {
            console.warn('Không có token được cấp.');
          }
        },
        (error) => {
          console.error('Lỗi khi yêu cầu quyền hoặc lấy token:', error);
        }
      );
  }

  listenToMessages() {
    this.afMessaging.messages
      .subscribe((message) => {
        console.log('New message:', message);
      });
  }
}

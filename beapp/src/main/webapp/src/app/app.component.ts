import {Component, HostListener} from '@angular/core';
import {UserService} from "./service/user.service";
import {Router} from "@angular/router";
import {TokenService} from "./service/token/token.service";
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'webapp';
  isTransparent = false;
  clickSearch = false;
  key: any;

  constructor(public userService: UserService,
              private router: Router,
              private tokenService: TokenService,
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
  navigateProfile(){
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
}

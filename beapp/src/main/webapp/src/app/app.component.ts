import {Component, HostListener, OnInit} from '@angular/core';
import {UserService} from "./service/user.service";
import {Router} from "@angular/router";
import {TokenService} from "./service/token/token.service";
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  title = 'webapp';
  isTransparent = false;
  clickSearch = false;
  key: any;
  isMobile: boolean = false;
  showMobileMenu: boolean = false;
  currentRole: any;
  constructor(public userService: UserService,
              private router: Router,
              private tokenService: TokenService,
              private breakpointObserver: BreakpointObserver
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

  ngOnInit(): void {
    this.breakpointObserver.observe([Breakpoints.Handset, Breakpoints.Tablet])
      .subscribe(result => {
        this.isMobile = result.matches;
      });
    this.currentRole = this.tokenService.getRoleUserFromToken();
  }
  openMobileMenu() {
    this.showMobileMenu = !this.showMobileMenu;  // Toggles the mobile menu visibility
  }
}

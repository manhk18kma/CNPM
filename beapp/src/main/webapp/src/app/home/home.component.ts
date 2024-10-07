import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit{
  isCommentsHidden = false;
  images: { url: string }[] = [];
  isMobileOrTablet: boolean = false;
  constructor(private breakpointObserver: BreakpointObserver) {
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

  toggleComments() {
    this.isCommentsHidden = !this.isCommentsHidden;
  }

  ngOnInit(): void {
    this.breakpointObserver.observe([Breakpoints.Handset, Breakpoints.Tablet])
      .subscribe(result => {
        this.isMobileOrTablet = result.matches;
      });
  }
}

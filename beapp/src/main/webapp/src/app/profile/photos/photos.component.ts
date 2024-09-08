import { Component } from '@angular/core';

@Component({
  selector: 'app-photos',
  templateUrl: './photos.component.html',
  styleUrls: ['./photos.component.scss']
})
export class PhotosComponent {
  photos = [
    { url: 'https://www.mountaingoatsoftware.com/uploads/blog/2016-09-06-what-is-a-product.png', title: 'Hình ảnh 1' },
    { url: 'https://www.mountaingoatsoftware.com/uploads/blog/2016-09-06-what-is-a-product.png', title: 'Hình ảnh 2' },
    { url: 'https://www.mountaingoatsoftware.com/uploads/blog/2016-09-06-what-is-a-product.png', title: 'Hình ảnh 3' },
    { url: 'https://www.mountaingoatsoftware.com/uploads/blog/2016-09-06-what-is-a-product.png', title: 'Hình ảnh 4' },
    { url: 'https://www.mountaingoatsoftware.com/uploads/blog/2016-09-06-what-is-a-product.png', title: 'Hình ảnh 5' },
    { url: 'https://www.mountaingoatsoftware.com/uploads/blog/2016-09-06-what-is-a-product.png', title: 'Hình ảnh 6' },
    { url: 'https://www.mountaingoatsoftware.com/uploads/blog/2016-09-06-what-is-a-product.png', title: 'Hình ảnh 7' },
    { url: 'https://www.mountaingoatsoftware.com/uploads/blog/2016-09-06-what-is-a-product.png', title: 'Hình ảnh 8' }
  ];
}

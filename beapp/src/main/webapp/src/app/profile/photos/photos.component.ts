import {Component, OnInit} from '@angular/core';
import {MediaService} from "../../service/media.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-photos',
  templateUrl: './photos.component.html',
  styleUrls: ['./photos.component.scss']
})
export class PhotosComponent implements OnInit{
  photos: any[] = [];
  private id: any;
  constructor(private mediaService: MediaService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.id = this.route.parent?.snapshot.paramMap.get('id');
    this.mediaService.getMediaBySeller(this.id).subscribe(res => {
      this.photos = res.data
      console.log(this.photos)
    })
  }
}

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {NgIf} from "@angular/common";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {provideToastr, ToastrModule} from "ngx-toastr";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import {CookieService} from "ngx-cookie-service";
import {AuthGuard} from "./service/auth/auth.guard";
import {TokenInterceptor} from "./service/token/token.interceptor";
import { SignupComponent } from './signup/signup.component';
import { ActiveComponent } from './active/active.component';
import { PostsComponent } from './profile/posts/posts.component';
import { InforComponent } from './profile/infor/infor.component';
import { PhotosComponent } from './profile/photos/photos.component';
import { ActivityComponent } from './profile/activity/activity.component';
import { PostComponent } from './post/post.component';
import { CartComponent } from './cart/cart.component';
import { OrderComponent } from './order/order.component';
import { MessageComponent } from './message/message.component';
import { DetailmessageComponent } from './message/detailmessage/detailmessage.component';
import { DeliveryComponent } from './order/delivery/delivery.component';
import { CompleteComponent } from './order/complete/complete.component';
import { CancelComponent } from './order/cancel/cancel.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    ProfileComponent,
    SignupComponent,
    ActiveComponent,
    PostsComponent,
    InforComponent,
    PhotosComponent,
    ActivityComponent,
    PostComponent,
    CartComponent,
    OrderComponent,
    MessageComponent,
    DetailmessageComponent,
    DeliveryComponent,
    CompleteComponent,
    CancelComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgIf,
    RouterLink,
    RouterLinkActive,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    NgbModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true}, CookieService,provideToastr(),AuthGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

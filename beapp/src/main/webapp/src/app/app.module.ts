import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {NgIf, NgOptimizedImage} from "@angular/common";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {BrowserAnimationsModule, provideAnimations} from "@angular/platform-browser/animations";
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
import {NgxSpinnerModule} from "ngx-spinner";
import { BankComponent } from './bank/bank.component';
import { AddressComponent } from './address/address.component';
import { RechargeComponent } from './recharge/recharge.component';
import { WithdrawComponent } from './withdraw/withdraw.component';
import { AddComponent } from './add/add.component';
import {MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";
import {MessageModule} from "primeng/message";
import {MessagesModule} from "primeng/messages";
import { LikedComponent } from './profile/liked/liked.component';
import { ResponseTransactionComponent } from './response-transaction/response-transaction.component';
import { DetailPostComponent } from './detail-post/detail-post.component';
import { RecaptchaModule, RecaptchaFormsModule } from 'ng-recaptcha';
import { PendingComponent } from './profile/pending/pending.component';
import { DeliveringComponent } from './profile/delivering/delivering.component';
import { SuccessComponent } from './profile/success/success.component';
import { CancelDeliveryComponent } from './profile/cancel-delivery/cancel-delivery.component';
import { WaitingComponent } from './order/waiting/waiting.component';
import { DetailOrderComponent } from './detail-order/detail-order.component';
import { ApproveWithdrawComponent } from './profile/approve-withdraw/approve-withdraw.component';
import { ApprovePostComponent } from './profile/approve-post/approve-post.component';

import {environment} from "./service/dto/env";
import { SearchComponent } from './search/search.component';
import {AngularFireModule} from "@angular/fire/compat";
import {AngularFireMessagingModule} from "@angular/fire/compat/messaging";
import { MessageFirebaseComponent } from './message-firebase/message-firebase.component';


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
    CancelComponent,
    BankComponent,
    AddressComponent,
    RechargeComponent,
    WithdrawComponent,
    AddComponent,
    LikedComponent,
    ResponseTransactionComponent,
    DetailPostComponent,
    PendingComponent,
    DeliveringComponent,
    SuccessComponent,
    CancelDeliveryComponent,
    WaitingComponent,
    DetailOrderComponent,
    ApproveWithdrawComponent,
    ApprovePostComponent,
    SearchComponent,
    MessageFirebaseComponent
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
    NgbModule,
    NgxSpinnerModule.forRoot({type: 'ball-clip-rotate'}),
    ToastModule,
    MessageModule,
    MessagesModule,
    RecaptchaModule,
    RecaptchaFormsModule,
    NgOptimizedImage,
    AngularFireMessagingModule,
    AngularFireModule.initializeApp(environment.firebaseConfig),
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true}, CookieService,provideToastr(),AuthGuard, provideAnimations(),MessageService,AngularFireModule, // required animations providers
    provideToastr(),
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

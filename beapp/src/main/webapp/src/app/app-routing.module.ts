import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {LoginComponent} from "./login/login.component";
import {ProfileComponent} from "./profile/profile.component";
import {AuthGuard} from "./service/auth/auth.guard";
import {SignupComponent} from "./signup/signup.component";
import {ActiveComponent} from "./active/active.component";
import {PostsComponent} from "./profile/posts/posts.component";
import {InforComponent} from "./profile/infor/infor.component";
import {PhotosComponent} from "./profile/photos/photos.component";
import {ActivityComponent} from "./profile/activity/activity.component";
import {CartComponent} from "./cart/cart.component";
import {OrderComponent} from "./order/order.component";
import {PostComponent} from "./post/post.component";
import {MessageComponent} from "./message/message.component";
import {DetailmessageComponent} from "./message/detailmessage/detailmessage.component";
import {DeliveryComponent} from "./order/delivery/delivery.component";
import {CompleteComponent} from "./order/complete/complete.component";
import {CancelComponent} from "./order/cancel/cancel.component";
import {BankComponent} from "./bank/bank.component";
import {AddComponent} from "./add/add.component";
import {RechargeComponent} from "./recharge/recharge.component";
import {WithdrawComponent} from "./withdraw/withdraw.component";
import {ResponseTransactionComponent} from "./response-transaction/response-transaction.component";
import {LikedComponent} from "./profile/liked/liked.component";
import {DetailPostComponent} from "./detail-post/detail-post.component";

const routes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {
    path: 'profile/:id',
    component: ProfileComponent,
    children: [
      {path: 'infor', component: InforComponent},
      {path: 'posts', component: PostsComponent},
      {path: 'photos', component: PhotosComponent},
      {path: 'activity', component: ActivityComponent},
      {path: 'liked', component: LikedComponent},
      // {path: '', redirectTo: 'infor', pathMatch: 'full'}
    ],canActivate: [AuthGuard]
  },
  {path: 'signup', component: SignupComponent,},
  {path: 'active/:token', component: ActiveComponent},
  {path: 'cart', component: CartComponent,canActivate: [AuthGuard]},
  {path: 'post', component: PostComponent},
  {
    path: 'message',
    component: MessageComponent,
    children: [
      {path: ':id', component: DetailmessageComponent},
    ],canActivate: [AuthGuard]
  },
  {
    path: 'order',
    component: OrderComponent,
    children: [
      {path: 'delivery', component: DeliveryComponent},
      {path: 'complete', component: CompleteComponent},
      {path: 'cancel', component: CancelComponent},
      {path: '', redirectTo: 'delivery', pathMatch: 'full'}
    ],canActivate: [AuthGuard]
  },
  {path: 'bank/:id', component: BankComponent,canActivate: [AuthGuard]},
  {path: 'add-bank/:id', component: AddComponent,canActivate: [AuthGuard]},
  {path: 'recharge/:id', component: RechargeComponent,canActivate: [AuthGuard]},
  {path: 'withdraw/:id', component: WithdrawComponent,canActivate: [AuthGuard]},
  {path: 'response-transaction', component: ResponseTransactionComponent,canActivate: [AuthGuard]},
  {path: 'post/:id', component: DetailPostComponent,canActivate: [AuthGuard]},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

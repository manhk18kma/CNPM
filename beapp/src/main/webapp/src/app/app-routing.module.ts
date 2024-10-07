import {NgModule, OnInit} from '@angular/core';
import {Router, RouterModule, Routes} from '@angular/router';
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
import {TokenService} from "./service/token/token.service";
import {PendingComponent} from "./profile/pending/pending.component";
import {DeliveringComponent} from "./profile/delivering/delivering.component";
import {SuccessComponent} from "./profile/success/success.component";
import {CancelDeliveryComponent} from "./profile/cancel-delivery/cancel-delivery.component";
import {AddressComponent} from "./address/address.component";
import {WaitingComponent} from "./order/waiting/waiting.component";
import {DetailOrderComponent} from "./detail-order/detail-order.component";
import {ApproveWithdrawComponent} from "./profile/approve-withdraw/approve-withdraw.component";
import {ApprovePostComponent} from "./profile/approve-post/approve-post.component";

const routes: Routes = []

function getRoutesBasedOnRole(tokenService: TokenService) {
  const role = tokenService.getRoleUserFromToken();
  const commonRoutes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: 'signup', component: SignupComponent},
    {
      path: 'message',
      component: MessageComponent,
      children: [
        {path: ':id', component: DetailmessageComponent},
      ], canActivate: [AuthGuard]
    },
    {path: 'active/:token', component: ActiveComponent},
  ];
  const roleBasedRoutes: { [key: string]: Routes } = {
    'ROLE_ADMIN': [
      {
        path: 'profile/:id',
        component: ProfileComponent,
        children: [
          {path: 'list-withdraws', component: ApproveWithdrawComponent},
          {path: 'list-posts', component: ApprovePostComponent},
        ], canActivate: [AuthGuard]
      },
    ],
    'ROLE_SHIPPER': [
      {
        path: 'profile/:id',
        component: ProfileComponent,
        children: [
          {path: 'pending', component: PendingComponent},
          {path: 'delivering', component: DeliveringComponent},
          {path: 'success', component: SuccessComponent},
          {path: 'cancel', component: CancelDeliveryComponent},
        ], canActivate: [AuthGuard]
      },
      {path: 'detail-order/:id', component: DetailOrderComponent, canActivate: [AuthGuard]},
    ],
    'ROLE_USER': [
      {path: '', redirectTo: 'home', pathMatch: 'full'},
      {path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
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
        ], canActivate: [AuthGuard]
      },
      {path: 'payment/:id', component: CartComponent, canActivate: [AuthGuard]},
      {path: 'post', component: PostComponent},
      {
        path: 'order',
        component: OrderComponent,
        children: [
          {path: 'delivery', component: DeliveryComponent},
          {path: 'complete', component: CompleteComponent},
          {path: 'cancel', component: CancelComponent},
          {path: 'waiting', component: WaitingComponent},
          {path: '', redirectTo: 'waiting', pathMatch: 'full'}
        ], canActivate: [AuthGuard]
      },
      {path: 'bank/:id', component: BankComponent, canActivate: [AuthGuard]},
      {path: 'add-bank/:id', component: AddComponent, canActivate: [AuthGuard]},
      {path: 'recharge/:id', component: RechargeComponent, canActivate: [AuthGuard]},
      {path: 'withdraw/:id', component: WithdrawComponent, canActivate: [AuthGuard]},
      {path: 'response-transaction', component: ResponseTransactionComponent, canActivate: [AuthGuard]},
      {path: 'post/:id', component: DetailPostComponent, canActivate: [AuthGuard]},
      {path: 'address', component: AddressComponent, canActivate: [AuthGuard]},

    ]
  };
  return [...commonRoutes, ...roleBasedRoutes[role] ?? []];
}

@NgModule({
  imports: [RouterModule.forRoot([])],
  exports: [RouterModule]
})
export class AppRoutingModule{
  constructor(
    private router: Router,
    private tokenService: TokenService // Inject TokenService via DI
  ) {
    const routes = getRoutesBasedOnRole(this.tokenService);  // Use DI to get TokenService
    this.router.resetConfig(routes);  // Dynamically reset the router configuration
  }
}

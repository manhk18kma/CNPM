import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {LoginComponent} from "./login/login.component";
import {ProfileComponent} from "./profile/profile.component";
import {AuthGuard} from "./service/auth/auth.guard";
import {SignupComponent} from "./signup/signup.component";

const routes: Routes = [
  {path: '',redirectTo: 'home',pathMatch: 'full'},
  {path:'home', component: HomeComponent},
  {path:'login', component: LoginComponent},
  {path:'profile', component: ProfileComponent,},
  {path:'signup', component: SignupComponent,},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

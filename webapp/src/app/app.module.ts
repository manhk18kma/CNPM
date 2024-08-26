import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {FormsModule} from "@angular/forms";
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

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    ProfileComponent,
    SignupComponent
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

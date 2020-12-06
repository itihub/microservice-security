import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  // 属性定义
  title = 'itihub microservice security';
  authenticated = false;
  order = {};

  constructor(private http: HttpClient) {

    this.http.get("me").subscribe(data => {
      if (data){
        this.authenticated = true;
      }
      if (!this.authenticated){
        window.location.href = 'http://auth.itihub.com:9090/oauth/authorize?' +
          'client_id=admin&' +
          'redirect_uri=http://admin.itihub.com:8080/oauth/callback&' +
          'response_type=code&' +
          'state=abc';
      }
    })
  }

  // 方法定义
  logout() {
    // 本地退出
    this.http.get('logout').subscribe(() => {
      this.authenticated = false ;
      // 认证服务器退出
      window.location.href = 'http://auth.itihub.com:9090/logout?redirect_uri=http://admin.itihub.com:8080';
    }, () => {
      alert('logout fail');
    })
  }

  getOrder() {
    this.http.get('api/order/orders/1').subscribe(data => {
      this.order = data;
    }, () => {
      alert('get order fail')
    })
  }
}

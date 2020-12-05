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
  credentials = {username:'itihub', password:'123456'};
  order = {id:null, productId:null};

  constructor(private http: HttpClient) {

  }

  // 方法定义
  login() {
    this.http.post('login', this.credentials).subscribe(() => {
      this.authenticated = true;

    }, () => {
      alert('login fail');
    })
  }

  logout() {
    this.http.get('logout').subscribe(() => {
      this.authenticated = false ;
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

import { Injectable } from "@angular/core";
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse
} from "@angular/common/http";
import { tap} from "rxjs/operators";
import { Observable} from "rxjs";
import { HttpClient} from "@angular/common/http";

/**
 * 自定义刷新token失败拦截器
 */
@Injectable()
export class RefreshInterceptor implements HttpInterceptor {

  constructor(private http: HttpClient) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap(
        () => {},
        error => {
          console.log(error)
          if (error.status === 500 && error.error.messages === 'refresh fail') {
            // refresh_token 过期处理方式： 退出登录
            // this.logout();

            // refresh_token 过期处理方式： 自动重新认证
            window.location.href = 'http://auth.itihub.com:9090/oauth/authorize?' +
              'client_id=admin&' +
              'redirect_uri=http://admin.itihub.com:8080/oauth/callback&' +
              'response_type=code&' +
              'state=abc';
          }
        }
      )
    );
  }

  logout() {
    this.http.get('logout', {}).subscribe(() => {
      window.location.href = 'http://auth.itihub.com:9090/logout?redirect_uri=http://admin.itihub.com:8080'
    });
  }

}

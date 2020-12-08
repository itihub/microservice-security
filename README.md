# 微服务安全  

## 项目模块    
├─microservice-security  
│  └─is-admin  前端项目    
│──   
│  └─is-order-api  order微服务/未引入安全网关前的微服务安全处理    
│──   
│  └─is-price-api  price微服务  
│──   
│  └─is-server-auth  认证服务  
│──   
│  └─is-server-gateway  安全网关  
│──   
│  └─is-user-api  单体项目API安全  

## host配置  
```
127.0.0.1   gateway.itihub.com
127.0.0.1   admin.itihub.com
127.0.0.1   auth.itihub.com
127.0.0.1   order.itihub.com
127.0.0.1   price.itihub.com
```

## OAuth 授权流程  

### 客户应用类型  

公开(客户标识)  
    + 单页应用SPA Angular、React、etc  
    + 原生APP iOS、Android  
私密(客户凭证)  
    + Web服务器端应用 .NET、Java、etc  
    + 服务/API 机器对机器  

### 授权类型    
+ 授权码模式(Authorization Code)  
    适用场景：Web、第三方原生APP    
    ```
    # 客户端应用前端服务器引导用户跳转到认证服务器进行认证后获取授权码  
    GET /oauth/authorize?client_id=admin&redirect_uri=http://admin.itihub.com:8080/oauth/callback&response_type=code HTTP/1.1
    Host: auth.itihub.com:9090
    
    # 使用client-id、client-secret和回调地址拿到授权码后去获取用户授权的令牌
    POST /oauth/token?redirect_uri=http://admin.itihub.com:8080/oauth/callback&grant_type=authorization_code&code=1234 HTTP/1.1
    Host: auth.itihub.com:9090
    Authorization: Basic YWRtaW46MTIzNDU2
    ```
+ 用户名密码(Resource Owner Credentials）  
    适用场景：第一方原生APP、第一方单页应用SPA        
    ```
    # 客户端应用使用client-id、client-secret和用户名密码去获取令牌
    POST /oauth/token?grant_type=password&password=123456&username=jimmy&scope=read write HTTP/1.1
    Host: localhost:9070
    Authorization: Basic YWRtaW46MTIzNDU2
    ```
+ 简化(Implicit)     
    适用场景：公开的浏览器单页应用    
    ```
    # 令牌直接从认证服务器返回
    GET /oauth/authorize?response_type=token&client_id=implicit&redirect_uri=https://www.baidu.com/&scope=read%20write&state=1234 HTTP/1.1
    Host: auth.itihub.com:9090 
    
    # 认证服务器认证成功后回调地址附加用户令牌
    https://www.baidu.com/#access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDc0MjM5ODYsInVzZXJfbmFtZSI6ImppemhlQGV4YW1wbGUuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIwYzA2ZDA3OS1lYzFjLTRlMzMtYWM5YS03MDA2MjA1ZWE3OWQiLCJjbGllbnRfaWQiOiJpbXBsaWNpdCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.Io0EBSufL9mpqT8lXEKOngcH-glGEgb7RtZ8zb-XOasOiXyqVWZKWFLfxmYnWeELow7trEdAuA5Fc61ifDkib_mT3_wQu4a_DLVDPiSxhV_g2KaiJevFaTyTJ5-Q6aK0pS85acRaAJ8vuaTI8lTIF-EaWcZQUemp92bFIXvIH3kGeoIwr-h_zJbQ6r7LAJ_Mqh-0_gmFpK3hj1TDZf_KLV29WB0gmOXjtAs2qtqv2GhNDi0MVuL_lMO9oHS1EoO_NXp55NOPm84veXCTkpFrnRCdFUp86rDGKZEwxJeuSja3UuB8tBNqRhS4mbvNX5_bVScnJL3GyFFlkEoyYaAZFg&token_type=bearer&state=1234&expires_in=3599&jti=0c06d079-ec1c-4e33-ac9a-7006205ea79d
    ```
+ 客户端凭证(Client Credentials)     
    适用场景：服务器间通信场景  
    ```
    # 使用client-id、client-secret获取令牌
    POST /oauth/token?grant_type=client_credentials HTTP/1.1
    Host: auth.itihub.com:9090
    Authorization: Basic Y2xpZW50X2NyZWRlbnRpYWxzOjEyMzQ1Ng==
    ```
类路径：```org.springframework.security.oauth2.provider.endpoint.TokenEndpoint```  


### 校验令牌   
```
# 校验token
GET /oauth/check_token?token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib3JkZXItc2VydmVyIl0sInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE2MDc0MjQ5OTgsImp0aSI6IjE3NzYyNjI1LTg2YTctNDU2Mi1iNGMzLTc5NDg3MGRiYjkyYiIsImNsaWVudF9pZCI6ImNsaWVudF9jcmVkZW50aWFscyJ9.WT3i7IlLUMwQMvLPuMnR5lUfIjpAfQx3sPjeOC4ImlE_Wm67E1QHC1Tl0vPTXaXVXd2UeNht0KZT62mYP43k05sW9ehN6WDZHO46pUjDwiRT8_D7jjS_PxYEUX2jHhvG83dx7tJlDCbDkPNxtCaI9oxywhDLKHJPWC0Qft_EcZocMXUJ8IoonFRMbahIOgZRXAJnMDnqbjtr4YW8Ogt5EgN0LL4QgHoER5S4FN99EN3GKDE_kQXq8qsNIIkVC4Rv4REVl7-FMzX7SACr30wWflHKrvD-FIXTP9y5VA0aNIKVRAQC89XVYmOci_ZIULLxI_OZ2V7TG4KfK8iry7hMvQ HTTP/1.1
Host: auth.itihub.com:9090
Authorization: Basic YWRtaW46MTIzNDU2
```
类路径：```org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint```    

### 获取公钥(JWT)    
```
# 使用client-id、client-secret获取令牌公钥  
GET /oauth/token_key HTTP/1.1
Host: auth.itihub.com:9090
Authorization: Basic b3JkZXJTZXJ2aWNlOjEyMzQ1Ng==
```
类路径：```org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint```    



## is-admin 项目初始化步骤  
1. 使用Maven安装Node和Npm包管理器构建开发环境  
执行```mvn frontend:install-node-and-npm```命令生成node文件夹  
执行```node --version```检查node版本  
执行```./npm --version```检查npm版本  

2. 安装angular cli 客户端  
执行```./npm install @angular/cli```命令安装angular cli   
执行```./ng --version```命令检查angular cli版本  

3. 使用angular cli初始化项目     
执行```./ng new my-app```命令初始化一个my-app的工程    
初始化选项  
    1. 不添加Angular routing  
    2. 使用CSS样式 
    ```
    ? Would you like to add Angular routing? No
    ? Which stylesheet format would you like to use? CSS
    ```  
4. 添加依赖  
```
./npm install bootstrap@3 jquery --save
./npm install ngx-cookie-service --save
```  


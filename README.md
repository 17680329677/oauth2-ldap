#### speech Oauth2 授权服务器

- spring boot project with Gradle

1. **授权endpoint配置**

- 根据需要可以在环境变量中为授权相关的endpoint配置统一的请求前缀，变量名为：**URL_PREFIX**， eg: 

```
URL_PREFIX=/kg-admin-panel
```
- 由于注解中不能使用变量，为了统一，需要为controller配置相同的api前缀，eg：
```
@RequestMapping("/kg-admin-panel")
```

2. **授权相关endpoint说明**

- 请求授权endpoint（即 授权服务入口）\
注： \
    1. 在未认证的情况下会自动重定向到登录页面进行认证
    2. 若在数据库中配置了redirect_uri（一个或多个），请求时的redirect_uri应当和数据库中的一个完全匹配，否则报错

```
http://host:port/kg-admin-panel（url_prefix）/oauth/authorize?response_type=code&client_id=xxx&redirect_uri=xxxx
```

- token申请endpoint \
申请token使用授权后在redirect页面获取的code进行申请，申请一次后code即作废

```
http://host:port/kg-admin-panel（url_prefix）/oauth/token?grant_type=authorization_code&client_id=client_1&client_secret=123456&code=Jzt0LcUKncpzudgAkMbxTNHe2rvK2P10
```

- refresh_token

```
http://127.0.0.1:8091/kg-admin-panel/oauth/token?grant_type=refresh_token&refresh_token=xxxxx&client_id=client_1&client_secret=123456
```

- token验证endpoint

```
http://host:port/kg-admin-panel（url_prefix）/oauth/check_token?token=xxx
```

3. **用户信息获取api**

注：此处将用户信息获取的api配置为资源，需要在请求头中加入有效的token才可获取用户信息

```
http://host:port/kg-admin-panel（url_prefix）/user/info
```

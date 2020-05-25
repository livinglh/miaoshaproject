[TOC]

# miaoshaproject
慕课网：SpringBoot构建电商基础秒杀项目

慕课网：聚焦Java性能优化 打造亿级流量秒杀系统

我的学习笔记：https://livinglh.top/


## 项目运行
1. 启动mysql服务
2. 启动redis
3. 启动rocketmq
- 先启动mqnameserver，然后启动broker

```
# cmd中
mqnamesrv
mqbroker.exe -n localhost:9876
```


4. 启动项目


## 基础能力建设

### SpringBoot + Mybatis + SpringMVC 基础项目的搭建

### 定义通用的返回对象（正确，错误，异常）

1. 定义了一个CommonReturnType类，controller层的所有方法固定返回该类，其中包含了staus和data两个属性以供前端解析使用
2. 定义了一个包装类BusinessException统一管理错误码（EmBusinessError）
3. 在BaseController里定义了一个通用的ExceptionHandler类解决未被controller层吸收的exception，使用errCode和errMsg的方式处理了BusinessException异常以及内部不可预知的异常，

## 模型能力建设

### 用户模型管理

otp短信获取，otp注册用户，用户手机登录

1. opt验证码的获取

2.  前端页面实现与美化（基于metronic模板）

3. 用户注册
   1. mapper中在.xml中添加查询详细信息的Sql配置
   2. 在mapper接口中添加上面的方法
   3. 在service接口中添加相应的方法和方法实现
   4. controller层添加相应的方法regist()，其中调用sevice层的方法
   5. 新建相应的前端文件register.html
4. 用户登录
5. 校验规则优化
   1. 新建ValidationResult.java校验结果返回类
   2. 新建ValidatorImpl.java校验方法实现类，其中调用Validator类的方法validate对相应的bean进行校验

### 商品模型管理

首先不要去动数据库，先设计商品领域模型

1. 创建商品
2. 商品列表查询
3. 商品详情查询
4. 商品列表到商品详情页的跳转

### 交易模型管理

场景：用户1个订单购买了1件商品，支付了1次费用

1. 校验下单状态
2. 落单减库存
3. 订单入库
4. 销量增加
5. 返回前端

### 秒杀模型管理

1. 定义活动模型
2. 考虑活动服务接口需要的方法
3. 在获取商品详情时获取活动信息，并将其聚合到商品信息模型中，返回前端展示
4. 商品详情前端页面修改，添加活动相关信息
5. 修改下单接口方法，使用前端url上传过来活动id，在下单接口内校验，并根据是否有活动，设置相应的价格


## 秒杀项目性能优化
### 核心知识点

![](http://live.livinglh.top/qiniuPicGo/20200427142808.png)



1. 项目框架回顾

   使用springboot搭建的秒杀项目回顾

   发现项目内隐含的问题

2. 性能压测框架

   springboot的云端部署

   使用JMeter发现性能问题

3. 分布式扩展

   Nginx反向代理负载均衡

   分布式会话管理

   使用redis实现分布式会话存储

4. 查询优化技术之多级缓存

   多级缓存设计原理

   Redis缓存，本地缓存

   热点Nginx Lua缓存

5. 查询优化技术之页面静态化

   静态请求CDN原理

   静态请求CDN应用

   PhantomJS商品详情全页面静态化

6. 交易优化技术之缓存库存

   交易缓存验证技术

   缓存库存模型

7. 交易优化技术之事务型消息

   事务型消息解决最终一致性

   库存售罄防击穿优化

8. 流量错峰技术

   秒杀令牌

   秒杀大闸

   队列泄洪

9. 防刷限流

   验证码

   限流器

   防黄牛

### 环境

![](http://live.livinglh.top/qiniuPicGo/20200427144240.png)

### 技术栈

SSM,SpringBoot

linux基本命令

mysql常用命令

Redis常用命令

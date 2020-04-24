# miaoshaproject
一个简单的 java springboot 秒杀项目


## 定义通用的返回对象（正确，错误，异常）
1. 定义了一个CommonReturnType,固定使用staus和data返回供前端解析使用
2. 定义了一个包装类BusinessException统一管理错误码（EmBusinessError）
3. 在BaseController里定义了一个通用的ExceptionHandler类解决未被controller层吸收的exception，
使用errCode和errMsg的方式处理了BusinessException异常以及内部不可预知的异常
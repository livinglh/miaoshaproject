# miaoshaproject
一个简单的 java springboot 秒杀项目

## 基础能力建设

### SpringBoot + Mybatis + SpringMVC 基础项目的搭建

### 定义通用的返回对象（正确，错误，异常）

1. 定义了一个CommonReturnType类，controller层的所有方法固定返回该类，其中包含了staus和data两个属性以供前端解析使用
2. 定义了一个包装类BusinessException统一管理错误码（EmBusinessError）
3. 在BaseController里定义了一个通用的ExceptionHandler类解决未被controller层吸收的exception，使用errCode和errMsg的方式处理了BusinessException异常以及内部不可预知的异常，

## 模型能力建设

### 用户信息管理

otp短信获取，otp注册用户，用户手机登录

1. opt验证码的获取

```java
@Autowired
private HttpServletRequest httpServletRequest;
// 用户获取otp短信接口
@RequestMapping("/getopt")
@ResponseBody
public CommonReturnType getOtp(@RequestParam(name="telphone") String telphone){
    // 需要按照一定的规则生成OTP验证码
    Random random = new Random();
    int randomInt = random.nextInt(99999);  //[0,99999)
    randomInt += 10000;
    String otpCode = String.valueOf(randomInt);

    // 将OTP验证码同对应用户的手机号关联 分布式：redis 这里简化使用httpsession方式绑定手机号与OTPCODE
    httpServletRequest.getSession().setAttribute(telphone,otpCode);

    // 将OTP验证码通过短信通道发送给用户，省略
    System.out.println("telphone = " + telphone + " & optCode = " + otpCode);
    return CommonReturnType.create(null);
}
```

2.  前端页面实现与美化（基于metronic模板）

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link href="static/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="static/assets/global/css/components.css" rel="stylesheet" type="text/css" />
    <link href="static/admin/pages/css/login.css" rel="stylesheet" type="text/css" />
    <script src="static/assets/global/plugins/jquery-1.11.0.min.js" type="text/javascript"></script>
</head>
<body class="login">
    <div class="content">
        <h3 class="form-title">获取otp信息</h3>
        <div class="form-group">
            <label class="control-label">手机号</label>
            <div>
                <input class="form-control" classtype="text" placeholder="手机号" name="telphone" id="telphone"/>
            </div>
            <div class="form-actions">
                <button class="btn blue" id="getotp" type="submit">
                    获取otp短信
                </button>
            </div>
        </div>
    </div>
</body>

<script>
    jQuery(document).ready(function () {
        // 绑定otp的click事件用于像后端发送获取手机验证码的请求
        $("#getotp").on("click",function () {
            var telphone = $("#telphone").val();
            if(telphone == null || telphone == ""){
                alert("手机号不能为空");
                return false;
            }

            $.ajax({
                type:"POST",
                contentType:"application/x-www-form-urlencoded",
                url:"http://localhost:8090/user/getotp",
                data:{
                    "telphone":$("#telphone").val(),
                },
                success:function (data) {
                    if(data.status == "success"){
                        alert("otp已经发送到了您的手机上，请注意查收");
                    }
                    else{
                        alert("otp发送失败，原因为" + data.data.errMsg);
                    }
                },
                error:function (data) {
                    alert("otp发送失败，原因为" + data.responseText);
                }
            })
            return false;
        })
    })

</script>
</html>
```

```java
# springboot该标签解决ajax跨域请求问题
@CrossOrigin
    
@RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
public CommonReturnType getOtp(@RequestParam(name="telphone") String telphone)
```

3. 用户注册
   1. controller层添加相应的方法regist()，其中调用sevice层的方法
   2. service层添加相应的方法rigist()
   3. 新建相应的前端文件register.html
4. 用户登录
5. 校验规则优化
   1. 新建ValidationResult.java校验结果返回类
   2. 新建ValidatorImpl.java校验方法实现类，其中调用Validator类的方法validate对相应的bean进行校验
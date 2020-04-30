package com.miaoshaproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller("user")
@RequestMapping("/user")
//@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@CrossOrigin(allowCredentials = "true", origins = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate redisTemplate;

    // 用户登录接口
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telphone") String telphone,
                                  @RequestParam(name="password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone)||
        org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        // 用户登录服务，用来校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telphone, this.EncodeByMd5(password));

//        // 将登录凭证加入到用户登录成功的session内
//        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
//        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        //方法2：基于token传输类似sessionid

        //修改成若用户登录验证成功，将对应的登录信息和登录凭证一起存入redis中
        //生成登录凭证token，采用UUID，保证唯一性
        String uuidToken= UUID.randomUUID().toString();
        uuidToken=uuidToken.replace("-","");
        //建立token和用户登录状态之间的联系
        redisTemplate.opsForValue().set(uuidToken,userModel);
        //设置超时时间,一小时
        redisTemplate.expire(uuidToken,1, TimeUnit.HOURS);

        //下发token
        return CommonReturnType.create(uuidToken);
    }

    // 用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType regist(@RequestParam(name="telphone") String telphone,
                                   @RequestParam(name="otpCode") String otpCode,
                                   @RequestParam(name="name") String name,
                                   @RequestParam(name="gender") Integer gender,
                                   @RequestParam(name="age") Integer age,
                                   @RequestParam(name="password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpCode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if(!StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        // 用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
//        userModel.setEncrptPassword(MD5Encoder.encode(password.getBytes()));
        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.rigist(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        // 加密字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    // 用户获取otp短信接口
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
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
        System.out.println("telphone = " + telphone + " & otpCode = " + otpCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        // 调用service服务
        UserModel userModel = userService.getUserById(id);

        // 若获取的对应用户信息不存在
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        // 将核心领域模型用户对象转化为可供UI使用的viewobject
        UserVO userVO = converFromModel(userModel);

        // 返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO converFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }


}

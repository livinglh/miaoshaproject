package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

public interface OrderService {

    //1. 前端url上传过来活动id，下单接口内校验，使用这种
    //2. 直接在下单接口内判断对应的商品是否存在秒杀活动，若存在进行中的则以秒杀价格下单
    OrderModel creatOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}

package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

public interface OrderService {
    OrderModel creatOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException;
}

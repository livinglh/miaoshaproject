package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

public interface PromoService {
    // 根据商品id获取即将进行或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);

    // 活动发布
    void publishpromo(Integer promoId);

    //生成秒杀用的令牌
    String generateSecondKillToken(Integer promoId,Integer itemId,Integer userId);
}

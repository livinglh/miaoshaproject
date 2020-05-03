package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    // 创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    // 浏览商品列表
    List<ItemModel> listItem();

    // 商品详情
    ItemModel getItemById(Integer id);

    // item及promo model缓存模型
    ItemModel getItemByIdInCache(Integer id);

    // 库存扣减
    boolean decreaseStock(Integer itemId, Integer amount);

    // 销量增加
    void increaseSales(Integer itemId, Integer amount);
}

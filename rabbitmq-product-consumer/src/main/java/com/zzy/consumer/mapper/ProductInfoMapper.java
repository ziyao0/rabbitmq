package com.zzy.consumer.mapper;

import com.zzy.consumer.entity.ProductInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 14:21
 * @desc
 */
@Repository
public interface ProductInfoMapper {

    public void updateProductStoreById(Integer productNo);

    public List<ProductInfo> getById(Integer productNo);
}

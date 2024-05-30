package com.travel.savingProduct.model.service;

import com.travel.savingProduct.model.SavingProductDto;
import com.travel.savingProduct.model.mapper.SavingProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SavingProductServiceImpl implements SavingProductService{
    private final SavingProductMapper savingMapper;


    @Override
    public List<SavingProductDto> searchSavingProduct(Map<String,Object> map) throws Exception {
        List<SavingProductDto> productList = savingMapper.searchSavingProduct(map);
        if(productList.isEmpty()){
            map.put("now","2024-01-01");
            productList = savingMapper.searchSavingProduct(map);
        }
        return productList;
    }
}


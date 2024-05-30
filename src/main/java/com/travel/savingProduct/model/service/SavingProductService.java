package com.travel.savingProduct.model.service;

import com.travel.savingProduct.model.SavingProductDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SavingProductService {
    List<SavingProductDto> searchSavingProduct(Map<String,Object> map) throws Exception;
}

package com.travel.savingProduct.model.mapper;

import com.travel.savingProduct.model.SavingProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SavingProductMapper {
    List<SavingProductDto> searchSavingProduct(Map<String,Object> map) throws SQLException;
}

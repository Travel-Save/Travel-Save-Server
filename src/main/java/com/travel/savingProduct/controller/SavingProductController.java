package com.travel.savingProduct.controller;

import com.travel.board.model.BoardDto;
import com.travel.savingProduct.model.SavingProductDto;
import com.travel.savingProduct.model.service.SavingProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SavingProductController {
    private final SavingProductServiceImpl savingService;

    @Operation(summary = "예/적금 상품 검색", description = "여행 경비와 기간에 따른 예/적금 상품을 검색한다.")
    @GetMapping("/search/product")
    public ResponseEntity<List<SavingProductDto>> searchSavingProduct(
            @RequestParam @Parameter(description = "예/적금 상품을 얻기위한 파라미터 (totalPrice, startDate, endDate)", required = true) Map<String,Object> map) throws Exception {
        log.info("searchSavingProduct - 호출 {}", map);
        return new ResponseEntity<List<SavingProductDto>>(savingService.searchSavingProduct(map), HttpStatus.CREATED);
    }

}

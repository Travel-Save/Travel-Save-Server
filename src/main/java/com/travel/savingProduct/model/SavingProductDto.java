package com.travel.savingProduct.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavingProductDto {
    private String kor_co_nm;
    private String fin_prdt_nm;
    private String join_way;
    private String mtrt_int;
    private String spcl_cnd;
    private String join_member;
    private String etc_note;
    private String max_limit;
    private String intr_rate_type;
    private String intr_rate_type_nm;
    private Long save_trm;
    private String rsrv_type_nm;
    private String intr_rate;
    private String intr_rate2;
}

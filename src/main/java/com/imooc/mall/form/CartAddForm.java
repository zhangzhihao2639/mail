package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CartAddForm {
    /**
     * 添加商品
     */
        @NotNull
        private Integer productId;

        private Boolean selected = true;

}

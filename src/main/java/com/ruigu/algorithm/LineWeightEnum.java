package com.ruigu.algorithm;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * 线路权重占比
 * @author hugangquan
 * @date 2021/06/06 18:31
 */
public enum LineWeightEnum {

    /**
     * 线路个数
     */
    LINE_COUNT(BigDecimal.valueOf(6)),
    /**
     * 节点个数
     */
    NODE_COUNT(BigDecimal.valueOf(3)),
    /**
     * 地址组个数
     */
    NODE_GROUP_COUNT(BigDecimal.valueOf(1)),
    ;

    @Getter
    private BigDecimal weight;

    private LineWeightEnum(BigDecimal weight){
        this.weight = weight;
    }

}

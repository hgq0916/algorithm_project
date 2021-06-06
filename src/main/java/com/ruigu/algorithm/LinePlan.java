package com.ruigu.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 线路方案
 * @author hugangquan
 * @date 2021/06/06 18:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinePlan {

    /**
     * 线路集合
     */
    private List<SimpleLineItem> simpleLineItems;

    /**
     * 权重
     */
    private BigDecimal weight = BigDecimal.ZERO;

    /**
     * 总途径节点个数
     */
    private int totalThroughNodeCount = 0;

    /**
     * 途径地址组个数
     */
    private int nodeThroughGroupCount = 0;

    /**
     * 线路数量
     */
    private int lineCount;

}

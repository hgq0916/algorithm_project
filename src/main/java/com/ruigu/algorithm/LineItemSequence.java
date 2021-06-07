package com.ruigu.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hugangquan
 * @date 2021/06/06 14:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineItemSequence {

    /**
     * 线路编码
     */
    private String lineCode;

    /**
     * 出发节点顺序
     */
    private Integer startNodeSequence;

    /**
     * 目的节点顺序
     */
    private Integer endNodeSequence;

}

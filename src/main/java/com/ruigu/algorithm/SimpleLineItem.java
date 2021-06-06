package com.ruigu.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hugangquan
 * @date 2021/06/06 14:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleLineItem {

    /**
     * 出发节点code
     */
    private String startNodeCode;

    /**
     * 目的节点code
     */
    private String endNodeCode;

    /**
     * 出发节点类型
     */
    private String startNodeType;

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

    /**
     * 该段线路经过的中转总节点个数
     */
    private int totalThroughNodeCount = 0;

    /**
     * 该段线路经过的中转地址组个数
     */
    private int nodeThroughGroupCount = 0;

}

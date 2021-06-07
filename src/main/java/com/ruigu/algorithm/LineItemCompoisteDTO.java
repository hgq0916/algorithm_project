package com.ruigu.algorithm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线路明细分组信息
 * @author hugangquan
 * @date 2021/6/7 10:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineItemCompoisteDTO {

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
     * 线路code及顺序 LINE21060511561300001-1-2,LINE21060514341000001-1-2
     */
    private String lineCodeAndSequence;

}

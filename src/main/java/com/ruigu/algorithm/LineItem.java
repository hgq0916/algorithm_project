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
public class LineItem {

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

    private List<LineSequence> lineSequences = new ArrayList<>();

}

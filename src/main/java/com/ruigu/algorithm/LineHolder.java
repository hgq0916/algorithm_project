package com.ruigu.algorithm;

import javafx.util.Pair;

import java.util.*;

/**
 * @author hugangquan
 * @date 2021/06/06 10:47
 */
public class LineHolder {

    /**
     * select current_node_code,current_node_type,next_node_code,GROUP_CONCAT(CONCAT(line_code,'-',node_sequence,'-',node_sequence+1))
     * from line_config_item where next_node_code !='' GROUP BY current_node_code,next_node_code;
     */

    public static final Map<Pair<String,String>,LineItem> lineMap = new HashMap<>();

    public static final Set<String> nodeSet = new HashSet();

    static {
        lineMap.put(new Pair<String, String>("NODE21022216564500001","NODE21022309560400001"), LineItem.builder()
                .startNodeCode("NODE21022216564500001")
                .endNodeCode("NODE21022309560400001")
                .startNodeType("warehouse_address")
                .lineSequences(Arrays.asList(LineSequence.builder()
                        .startNodeSequence(2)
                        .endNodeSequence(3)
                        .lineCode("LINE21052016390800001")
                        .build()))
                .build()
        );
        lineMap.put(new Pair<String, String>("NODE21022415444800001","NODE21022216564500001"),LineItem.builder()
                .startNodeCode("NODE21022415444800001")
                .endNodeCode("NODE21022216564500001")
                .startNodeType("warehouse_address")
                .lineSequences(Arrays.asList(LineSequence.builder()
                        .startNodeSequence(1)
                        .endNodeSequence(2)
                        .lineCode("LINE21052016390800001")
                        .build()))
                .build()
                );
        lineMap.put(new Pair<String, String>("NODE21031614061200001","NODE21060510320100001"),LineItem.builder()
                .startNodeCode("NODE21031614061200001")
                .endNodeCode("NODE21060510320100001")
                .startNodeType("warehouse_address")
                .lineSequences(Arrays.asList(LineSequence.builder()
                        .startNodeSequence(1)
                        .endNodeSequence(2)
                        .lineCode("LINE21060511475500001")
                        .build()))
                .build());
        lineMap.put(new Pair<String, String>("NODE21031614061200001","NODE21060511104700001"),LineItem.builder()
                .startNodeCode("NODE21031614061200001")
                .endNodeCode("NODE21060511104700001")
                .startNodeType("warehouse_address")
                .lineSequences(Arrays.asList(LineSequence.builder()
                    .startNodeSequence(1)
                    .endNodeSequence(2)
                    .lineCode("LINE21060511561300001")
                    .build(),
                    LineSequence.builder()
                    .startNodeSequence(1)
                    .endNodeSequence(2)
                    .lineCode("LINE21060514341000001")
                    .build()
                ))
                .build());
        lineMap.put(new Pair<String, String>("NODE21060510320100001","NODE21031614061200001"), LineItem.builder()
                .startNodeCode("NODE21060510320100001")
                .endNodeCode("NODE21031614061200001")
                .startNodeType("warehouse_address")
                .lineSequences(Arrays.asList(LineSequence.builder()
                        .startNodeSequence(2)
                        .endNodeSequence(3)
                        .lineCode("LINE21060511475500001")
                        .build()))
                .build());
        lineMap.put(new Pair<String, String>("NODE21060510320100001","NODE21060510340100001"),LineItem.builder()
                .startNodeCode("NODE21060510320100001")
                .endNodeCode("NODE21060510340100001")
                .startNodeType("warehouse_address")
                .lineSequences(Arrays.asList(LineSequence.builder()
                                .startNodeSequence(1)
                                .endNodeSequence(2)
                                .lineCode("LINE21060511531500001")
                                .build(),
                        LineSequence.builder()
                                .startNodeSequence(1)
                                .endNodeSequence(2)
                                .lineCode("LINE21060514241100001")
                                .build()
                ))
                .build());


        lineMap.put(new Pair<String, String>("NODE21060510340100001","NODE21060510575200001"),LineItem.builder()
                .startNodeCode("NODE21060510340100001")
                .endNodeCode("NODE21060510575200001")
                .startNodeType("warehouse_address")
                .lineSequences(Arrays.asList(LineSequence.builder()
                                .startNodeSequence(2)
                                .endNodeSequence(3)
                                .lineCode("LINE21060511531500001")
                                .build(),
                        LineSequence.builder()
                                .startNodeSequence(2)
                                .endNodeSequence(3)
                                .lineCode("LINE21060514241100001")
                                .build()
                ))
                .build());
        lineMap.put(new Pair<String, String>("NODE21060510575200001","NODE21060510320100001"),LineItem.builder()
                .startNodeCode("NODE21060510575200001")
                .endNodeCode("NODE21060510320100001")
                .startNodeType("warehouse_address")
                .lineSequences(Arrays.asList(LineSequence.builder()
                        .startNodeSequence(3)
                        .endNodeSequence(4)
                        .lineCode("LINE21060514241100001")
                        .build()))
                .build());
        lineMap.put(new Pair<String, String>("NODE21060511104700001","NODE21060511195000001"),LineItem.builder()
                .startNodeCode("NODE21060511104700001")
                .endNodeCode("NODE21060511195000001")
                .startNodeType("warehouse_address")
                .lineSequences(Arrays.asList(LineSequence.builder()
                                .startNodeSequence(2)
                                .endNodeSequence(3)
                                .lineCode("LINE21060511561300001")
                                .build(),
                        LineSequence.builder()
                                .startNodeSequence(2)
                                .endNodeSequence(3)
                                .lineCode("LINE21060514341000001")
                                .build()
                ))
                .build());
        lineMap.put(new Pair<String, String>("NODE21060511195000001","NODE21031614061200001"),LineItem.builder()
                .startNodeCode("NODE21060511195000001")
                .endNodeCode("NODE21031614061200001")
                .startNodeType("warehouse_address")
                .lineSequences(Arrays.asList(LineSequence.builder()
                        .startNodeSequence(3)
                        .endNodeSequence(4)
                        .lineCode("LINE21060514341000001")
                        .build()))
                .build());

        nodeSet.add("NODE21022216564500001");
        nodeSet.add("NODE21022415444800001");
        nodeSet.add("NODE21031614061200001");
        nodeSet.add("NODE21031614061200001");
        nodeSet.add("NODE21060510320100001");
        nodeSet.add("NODE21060510320100001");
        nodeSet.add("NODE21060510340100001");
        nodeSet.add("NODE21060510575200001");
        nodeSet.add("NODE21060511104700001");
        nodeSet.add("NODE21060511195000001");
        nodeSet.add("NODE21022309560400001");
        nodeSet.add("NODE21022216564500001");
        nodeSet.add("NODE21060510320100001");
        nodeSet.add("NODE21060511104700001");
        nodeSet.add("NODE21031614061200001");
        nodeSet.add("NODE21060510340100001");
        nodeSet.add("NODE21060510575200001");
        nodeSet.add("NODE21060510320100001");
        nodeSet.add("NODE21060511195000001");
        nodeSet.add("NODE21031614061200001");


    }

}

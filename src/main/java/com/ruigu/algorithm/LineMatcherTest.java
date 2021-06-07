package com.ruigu.algorithm;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 线路匹配测试
 * @author hugangquan
 * @date 2021/6/7 14:40
 */
public class LineMatcherTest {

    public static void main(String[] args) {

        List<LineItemCompoisteDTO> lineItemCompoisteDTOS = new ArrayList<>();

        Map<Pair, LineItemDetailInfo> lineItemDetailInfoMap = lineItemCompoisteDTOS.stream().parallel()
                .collect(Collectors.toMap(
                dto -> new Pair(dto.getStartNodeCode(), dto.getEndNodeCode()), dto -> {

                    LineItemDetailInfo lineItemDetailInfo = LineItemDetailInfo.builder()
                            .startNodeCode(dto.getStartNodeCode())
                            .endNodeCode(dto.getEndNodeCode())
                            .startNodeType(dto.getStartNodeType())
                            .build();

                    String lineCodeAndSequence = dto.getLineCodeAndSequence();

                    String[] lines = lineCodeAndSequence.split(",");

                    List<LineItemSequence> lineItemSequences = new ArrayList<>();
                    for(String line : lines){
                        String[] lineInfo = line.split("-");
                        LineItemSequence lineItemSequence = LineItemSequence.builder()
                                .lineCode(lineInfo[0])
                                .startNodeSequence(Integer.parseInt(lineInfo[1]))
                                .endNodeSequence(Integer.parseInt(lineInfo[2]))
                                .build();
                        lineItemSequences.add(lineItemSequence);
                    }

                    lineItemDetailInfo.setLineItemSequences(lineItemSequences);

                    return lineItemDetailInfo;

                }
        ));

        Map<Pair<String, String>, LineItemDetailInfo> lineMap = new HashMap<>(LineHolder.lineMap);

        LineMatcher lineMatcher = new LineMatcher(lineMap);

        List<LinePlan> linePlans = lineMatcher.matches("NODE21060510340100001", "NODE21031614061200001");

        printLines(linePlans);

    }

    private static void printLines(List<LinePlan> lineList) {

        int i=1;
        for(LinePlan linePlan:lineList){
            StringBuffer buffer = new StringBuffer("第"+(i++)+"条方案:");
            for(SimpleLineItem lineItem:linePlan.getSimpleLineItems()){
                buffer.append(lineItem.getLineCode()).append(":")
                        .append(lineItem.getStartNodeCode())
                        .append("(")
                        .append(lineItem.getStartNodeType())
                        .append(")")
                        .append("--")
                        .append(lineItem.getEndNodeCode())
                        .append("[")
                        .append(lineItem.getStartNodeSequence())
                        .append("-")
                        .append(lineItem.getEndNodeSequence())
                        .append("]")
                        .append("-->");
            }
            System.out.println(buffer.substring(0,buffer.length()-3));
        }

    }

}

package com.ruigu.algorithm;

import javafx.util.Pair;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 线路匹配器
 * @author hugangquan
 * @date 2021/06/06 09:38
 */
public class LineMatcher {

    private Map<Pair<String,String>,LineItemDetailInfo> lineMap;

    private Set<String> nodeSet = new HashSet<String>();

    public LineMatcher( Map<Pair<String,String>,LineItemDetailInfo> lineMap){
        this.lineMap = lineMap;
        //记录所有的节点
        for(Pair<String,String> pair: lineMap.keySet()){
            nodeSet.add(pair.getKey());
            nodeSet.add(pair.getValue());
        }
    }

    /**
     * 根据起点和终点获取所有的节点路径
     * @param startNode 起点
     * @param endNode 终点
     * @return
     */
    private List<LinkedList<String>> dfs(String startNode, String endNode){
        LineDFS lineDFS = new LineDFS(startNode,endNode);
        return lineDFS.dfs();
    }

    private class LineDFS {

        /**
         * 出发节点
         */
        private String start;

        /**
         * 目的节点
         */
        private String end;

        /**
         * 已访问过的节点
         */
        private Set<String> visit = new HashSet<String>();

        /**
         * 节点路径
         */
        private LinkedList<String> stack = new LinkedList<String>();

        /**
         * 匹配到的节点路径
         */
        private List<LinkedList<String>> lines = new ArrayList<>();

        LineDFS(String startNode, String endNode) {
            this.start = startNode;
            this.end = endNode;
        }

        List<LinkedList<String>> dfs(){
            dfs(start);
            return lines;
        }

        void dfs(String pos){

            if(pos.equals(end)){
                LinkedList<String> line = new LinkedList<>(stack);
                line.addLast(end);
                lines.add(line);
            }

            //将该节点标记为已访问
            visit.add(pos);

            //将该节点入栈
            stack.addLast(pos);

            for(String node:LineMatcher.this.nodeSet){
                if(!visit.contains(node) && LineMatcher.this.lineMap.containsKey(new Pair<String, String>(pos,node))){
                    dfs(node);
                }
            }

            //将该节点标记为未访问
            visit.remove(pos);
            //将该节点出栈
            stack.pollLast();
        }

    }

    /**
     * 根据起点和终点匹配最优线路
     * @param startNodeCode
     * @param endNodeCode
     * @return
     */
    public LinePlan matchesOptimal(String startNodeCode,String endNodeCode) {
        List<LinePlan> matches = matches(startNodeCode, endNodeCode);
        if(matches.size()>0){
            return matches.get(0);
        }

        return null;
    }

    /**
     * 根据起点和终点匹配所有线路
     * @param startNodeCode
     * @param endNodeCode
     * @return
     */
    public List<LinePlan> matches(String startNodeCode,String endNodeCode) {

        if(!nodeSet.contains(startNodeCode) || !nodeSet.contains(endNodeCode)){
            //节点不包含起点或终点的任意一个
            return new ArrayList<>();
        }

        List<LinkedList<String>> nodePaths = dfs(startNodeCode, endNodeCode);

        if(nodePaths.isEmpty()){
            return new ArrayList<>();
        }

        List<List<LineItemDetailInfo>> lineList = new ArrayList<>();

        for(LinkedList<String> line : nodePaths){
            String preNode = null;
            List<LineItemDetailInfo> list = new ArrayList<>();
            for(String node:line){
                if(preNode != null){
                    LineItemDetailInfo lineItemDetailInfo = lineMap.get(new Pair<>(preNode, node));
                    list.add(lineItemDetailInfo);
                }
                preNode = node;
            }
            lineList.add(list);
        }

        //线路拆分 1*2*3
        List<List<SimpleLineItem>> lists = splitLine(lineList);

        /**
         * 线路合并
         */
        List<List<SimpleLineItem>> mergeList = mergeLine(lists);

        //淘汰中转点为地址组的线路
        eliminateAddressGroupLine(mergeList);

        //创建线路规划
        List<LinePlan> linePlans = mergeList.stream().map(
                list -> createLinePlan(list)
        ).collect(Collectors.toList());

        /**
         * 对线路排序,根据权重比较线路的优劣
         */
        linePlans.sort((plan1,plan2)-> {
            if(plan1.getWeight().compareTo(plan2.getWeight())>0){
                return -1;
            } else if(plan1.getWeight().compareTo(plan2.getWeight())<0){
                return 1;
            }

            return 0;
        });

        return linePlans;
    }

    /**
     * 创建线路规划
     * @param simpleLineItems 线路集合
     * @return
     */
    private LinePlan createLinePlan(List<SimpleLineItem> simpleLineItems) {
        LinePlan linePlan = LinePlan.builder()
                .simpleLineItems(simpleLineItems)
                .lineCount(simpleLineItems.size())
                .build();

        int totalThroughNodeCount = 0;
        int nodeThroughGroupCount = 0;

        for(int i=0;i<simpleLineItems.size();i++){
            SimpleLineItem simpleLineItem = simpleLineItems.get(i);
            if(i!=0){
                totalThroughNodeCount += 1;
                if("delivery_collection_group".equals(simpleLineItem.getStartNodeType())){
                    nodeThroughGroupCount += 1;
                }
            }
            totalThroughNodeCount += simpleLineItem.getTotalThroughNodeCount();
            nodeThroughGroupCount += simpleLineItem.getNodeThroughGroupCount();
        }

        linePlan.setTotalThroughNodeCount(totalThroughNodeCount);
        linePlan.setNodeThroughGroupCount(nodeThroughGroupCount);

        //计算权重(最大权重10): 线路个数(6)，节点个数(3)，地址组个数(1)
        BigDecimal weight = BigDecimal.ZERO;

        //使用的线路越少，权重越高
        weight = weight.add(LineWeightEnum.LINE_COUNT.getWeight().divide(BigDecimal.valueOf(linePlan.getLineCount())));

        //使用的节点个数越少，权重越高
        weight = weight.add(LineWeightEnum.NODE_COUNT.getWeight().divide(BigDecimal.valueOf(linePlan.getTotalThroughNodeCount()+1)));

        //使用对的地址组个数越少，权重越高
        weight = weight.add(LineWeightEnum.NODE_GROUP_COUNT.getWeight().divide(BigDecimal.valueOf(linePlan.getNodeThroughGroupCount()+1)));

        linePlan.setWeight(weight);

        return linePlan;
    }

    /**
     * 淘汰中转点为地址组的线路
     * @param simpleLineItems
     */
    private void eliminateAddressGroupLine(List<List<SimpleLineItem>> simpleLineItems) {
        Iterator<List<SimpleLineItem>> iterator = simpleLineItems.iterator();
        while (iterator.hasNext()){
            List<SimpleLineItem> next = iterator.next();
            for(SimpleLineItem simpleLineItem : next){
                if(NodeTypeEnum.DELIVERY_COLLECTION_GROUP.getCode().equals(simpleLineItem.getStartNodeType())){
                    iterator.remove();
                    break;
                }
            }
        }
    }

    /**
     * 将同一条线路的多个节点合并
     * @param lists
     * @return
     */
    private List<List<SimpleLineItem>> mergeLine(List<List<SimpleLineItem>> lists) {
        List<List<SimpleLineItem>> mergeList = new ArrayList<>();

        //线路合并
        for(List<SimpleLineItem> lineItems : lists){

            SimpleLineItem preItem= null;
            List<SimpleLineItem> newItem = new ArrayList<>();
            for(SimpleLineItem item: lineItems){
                if(preItem != null){
                    //和上一节点比较线路
                    if(preItem.getLineCode().equals(item.getLineCode())){
                        //合并节点
                        preItem.setEndNodeCode(item.getEndNodeCode());
                        preItem.setEndNodeSequence(item.getEndNodeSequence());
                        preItem.setTotalThroughNodeCount(preItem.getTotalThroughNodeCount()+1);
                        if(NodeTypeEnum.DELIVERY_COLLECTION_GROUP.getCode().equals(item.getStartNodeType())){
                            preItem.setNodeThroughGroupCount(preItem.getNodeThroughGroupCount()+1);
                        }
                        continue;
                    }
                }
                newItem.add(item);
                preItem = item;
            }
            mergeList.add(newItem);
        }

        return mergeList;
    }

    /**
     * 拆分线路
     * @param lines
     * @return
     */
    private static List<List<SimpleLineItem>> splitLine(List<List<LineItemDetailInfo>> lines) {

        List<List<SimpleLineItem>> lists = new ArrayList<>();

       for(List<LineItemDetailInfo> lineItemDetailInfos : lines){
           List<List<SimpleLineItem>> simpleLineItems = splitLineItem(lineItemDetailInfos);
           lists.addAll(simpleLineItems);
       }

        return lists;

    }

    private static List<List<SimpleLineItem>> splitLineItem(List<LineItemDetailInfo> lineItemDetailInfos) {

        //存储线路
        List<List<SimpleLineItem>> lists = new ArrayList<>();

        //记录访问的节点
        LinkedList<SimpleLineItem> stack = new LinkedList<>();

        splitLineItem(lists,stack, lineItemDetailInfos,0);

        return lists;
    }

    /**
     * 递归拆分线路
     * @param lists
     * @param stack
     * @param lineItemDetailInfos
     * @param index
     */
    private static void splitLineItem(List<List<SimpleLineItem>> lists, LinkedList<SimpleLineItem> stack,
                                      List<LineItemDetailInfo> lineItemDetailInfos, int index) {

        LineItemDetailInfo lineItemDetailInfo = lineItemDetailInfos.get(index);
        if(index == lineItemDetailInfos.size()-1){

            List<LineItemSequence> lineItemSequences = lineItemDetailInfo.getLineItemSequences();

            for(LineItemSequence lineItemSequence : lineItemSequences){
                List<SimpleLineItem> simpleLineItems = new ArrayList<>(stack);
                simpleLineItems.add(SimpleLineItem.builder()
                        .startNodeCode(lineItemDetailInfo.getStartNodeCode())
                        .endNodeCode(lineItemDetailInfo.getEndNodeCode())
                        .startNodeSequence(lineItemSequence.getStartNodeSequence())
                        .endNodeSequence(lineItemSequence.getEndNodeSequence())
                        .lineCode(lineItemSequence.getLineCode())
                        .startNodeType(lineItemDetailInfo.getStartNodeType())
                        .build());
                lists.add(simpleLineItems);
            }
            return;
        }

        List<LineItemSequence> lineItemSequences = lineItemDetailInfo.getLineItemSequences();

        for(LineItemSequence lineItemSequence : lineItemSequences){

            stack.add(SimpleLineItem.builder()
                    .startNodeCode(lineItemDetailInfo.getStartNodeCode())
                    .endNodeCode(lineItemDetailInfo.getEndNodeCode())
                    .startNodeSequence(lineItemSequence.getStartNodeSequence())
                    .endNodeSequence(lineItemSequence.getEndNodeSequence())
                    .lineCode(lineItemSequence.getLineCode())
                    .startNodeType(lineItemDetailInfo.getStartNodeType())
                    .build());

            splitLineItem(lists,stack, lineItemDetailInfos,index+1);

            stack.pollLast();
        }


    }

}

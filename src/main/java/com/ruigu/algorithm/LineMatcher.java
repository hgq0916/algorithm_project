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

    private Set<String> visit = new HashSet<String>();

    private LinkedList<String> stack = new LinkedList<String>();

    private List<LinkedList<String>> lines = new ArrayList<>();

    private String start;

    private String end;

    public LineMatcher(String start, String end){
        this.start = start;
        this.end = end;
    }

    public List<LinkedList<String>> dfs(){
        dfs(start);
        return lines;
    }

    public  void dfs(String pos){

        if(pos.equals(end)){
            LinkedList<String> line = new LinkedList<>(stack);
            line.addLast(end);
            lines.add(line);
        }

        //将该节点标记为已访问
        visit.add(pos);

        //将该节点入栈
        stack.addLast(pos);

        for(String node:LineHolder.nodeSet){
            if(!visit.contains(node) && LineHolder.lineMap.containsKey(new Pair<String, String>(pos,node))){
                dfs(node);
            }
        }

        //将该节点标记为未访问
        visit.remove(pos);
        //将该节点出栈
        stack.pollLast();
    }

    public static void main(String[] args) throws IOException {
        LineMatcher lineMatcher = new LineMatcher("NODE21060510340100001", "NODE21031614061200001");

        List<List<LineItem>> lineList = new ArrayList<>();

        List<LinkedList<String>> lines = lineMatcher.dfs();
        for(LinkedList<String> line : lines){
            String preNode = null;
            List<LineItem> list = new ArrayList<>();
            for(String node:line){
                if(preNode != null){
                    LineItem lineItem = LineHolder.lineMap.get(new Pair<>(preNode, node));
                    list.add(lineItem);
                }
                preNode = node;
            }
            lineList.add(list);
        }

        //线路拆分 1*2*3
        List<List<SimpleLineItem>> lists = splitLine(lineList);

        List<List<SimpleLineItem>> newList = new ArrayList<>();

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
                        if("delivery_collection_group".equals(item.getStartNodeType())){
                            preItem.setNodeThroughGroupCount(preItem.getNodeThroughGroupCount()+1);
                        }
                        continue;
                    }
                }
                newItem.add(item);
                preItem = item;
            }
            newList.add(newItem);
        }

        //淘汰中转点为地址组的线路
        Iterator<List<SimpleLineItem>> iterator = newList.iterator();
        while (iterator.hasNext()){
            List<SimpleLineItem> next = iterator.next();
            for(SimpleLineItem simpleLineItem : next){
                if("delivery_collection_group".equals(simpleLineItem.getStartNodeType())){
                    iterator.remove();
                    break;
                }
            }
        }

        List<LinePlan> linePlans = newList.stream().map(
                list -> {
                    LinePlan linePlan = LinePlan.builder()
                            .simpleLineItems(list)
                            .lineCount(list.size())
                            .build();

                    int totalThroughNodeCount = 0;
                    int nodeThroughGroupCount = 0;

                    for(int i=0;i<list.size();i++){
                        SimpleLineItem simpleLineItem = list.get(i);
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
        ).collect(Collectors.toList());

        /**
         * 对线路排序,按照中转次数从小到大排序，如果中转次数相同，比较途径节点的个数，
         * 如果途径节点个数相同，则比较经过的地址组个数
         */
        linePlans.sort((plan1,plan2)-> {
            if(plan1.getWeight().compareTo(plan2.getWeight())>0){
                return -1;
            } else if(plan1.getWeight().compareTo(plan2.getWeight())<0){
                return 1;
            }

            return 0;
        });

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

    /**
     * 拆分线路
     * @param lines
     * @return
     */
    private static List<List<SimpleLineItem>> splitLine(List<List<LineItem>> lines) {

        List<List<SimpleLineItem>> lists = new ArrayList<>();

       for(List<LineItem> lineItems: lines){
           List<List<SimpleLineItem>> simpleLineItems = splitLineItem(lineItems);
           lists.addAll(simpleLineItems);
       }

        return lists;

    }

    private static List<List<SimpleLineItem>> splitLineItem(List<LineItem> lineItems) {

        //存储线路
        List<List<SimpleLineItem>> lists = new ArrayList<>();

        //记录访问的节点
        LinkedList<SimpleLineItem> stack = new LinkedList<>();

        splitLineItem(lists,stack,lineItems,0);

        return lists;
    }

    private static void splitLineItem(List<List<SimpleLineItem>> lists,LinkedList<SimpleLineItem> stack,
                                     List<LineItem> lineItems, int index) {

        LineItem lineItem = lineItems.get(index);
        if(index == lineItems.size()-1){

            List<LineSequence> lineSequences = lineItem.getLineSequences();

            for(LineSequence lineSequence: lineSequences){
                List<SimpleLineItem> simpleLineItems = new ArrayList<>(stack);
                simpleLineItems.add(SimpleLineItem.builder()
                        .startNodeCode(lineItem.getStartNodeCode())
                        .endNodeCode(lineItem.getEndNodeCode())
                        .startNodeSequence(lineSequence.getStartNodeSequence())
                        .endNodeSequence(lineSequence.getEndNodeSequence())
                        .lineCode(lineSequence.getLineCode())
                        .startNodeType(lineItem.getStartNodeType())
                        .build());
                lists.add(simpleLineItems);
            }
            return;
        }

        List<LineSequence> lineSequences = lineItem.getLineSequences();

        for(LineSequence lineSequence:lineSequences){

            stack.add(SimpleLineItem.builder()
                    .startNodeCode(lineItem.getStartNodeCode())
                    .endNodeCode(lineItem.getEndNodeCode())
                    .startNodeSequence(lineSequence.getStartNodeSequence())
                    .endNodeSequence(lineSequence.getEndNodeSequence())
                    .lineCode(lineSequence.getLineCode())
                    .startNodeType(lineItem.getStartNodeType())
                    .build());

            splitLineItem(lists,stack,lineItems,index+1);

            stack.pollLast();
        }


    }

}

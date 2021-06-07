package com.ruigu.algorithm;

import lombok.Getter;

/**
 * @author GengMengyue
 * @date 2021/1/5
 */
public enum NodeTypeEnum {
    /**
     * 仓库
     */
    WAREHOUSE("warehouse_address","仓库地址"),
    /**
     * 店铺
     */
    DELIVERY_COLLECTION_GROUP("delivery_collection_group","派送/揽收地址组");

    @Getter
    private String code;


    @Getter
    private String msg;

    private NodeTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据code获取message
     * @param code
     * @return
     */
    public static NodeTypeEnum of(String code){
        NodeTypeEnum[] nodeTypeEnums = NodeTypeEnum.values();
        for(NodeTypeEnum nodeTypeEnum : nodeTypeEnums){
            if(nodeTypeEnum.code.equals(code)){
                return nodeTypeEnum;
            }
        }
        return null;
    }
}

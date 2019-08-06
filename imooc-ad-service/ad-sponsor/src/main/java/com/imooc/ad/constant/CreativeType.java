package com.imooc.ad.constant;


import lombok.Getter;

/**
 * @Author: hekai
 * @Date: 2019-07-31 17:01
 */
@Getter
public enum CreativeType {
    /**
     * 图片
     */
    IMAGE(1, "图片"),
    /**
     * 视频
     */
    VIDEO(2, "视频"),
    /**
     * 文本
     */
    TEXT(3, "文本");

    private Integer type;
    private String desc;

    CreativeType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}

package com.imooc.ad.constant;

import lombok.Getter;

/**
 * @Author: hekai
 * @Date: 2019-07-31 17:04
 */
@Getter
public enum CreativeMaterialType {
    /**
     * 图片
     */
    JPG(1, "jpg"),
    BMP(2, "bmp"),
    /**
     * 视频
     */
    MP4(3, "mp4"),
    AVI(4, "avi"),
    /**
     * 文本
     */
    TXT(5, "txt");

    private Integer type;
    private String desc;

    CreativeMaterialType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}

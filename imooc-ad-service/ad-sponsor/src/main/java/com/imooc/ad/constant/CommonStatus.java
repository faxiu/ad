package com.imooc.ad.constant;

import lombok.Getter;

/**
 * @Author: hekai
 * @Date: 2019-07-31 15:27
 */
@Getter
public enum CommonStatus {
    /**
     * 有效状态
     */
    VALID(1, "有效状态"),
    /**
     * 无效状态
     */
    INVALID(0, "无效状态");

    private Integer status;
    private String desc;

    CommonStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

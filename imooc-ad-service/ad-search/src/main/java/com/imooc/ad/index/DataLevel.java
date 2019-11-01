package com.imooc.ad.index;

import lombok.Getter;

/**
 * @Author: hekai
 * @Date: 2019-08-12 16:51
 */
@Getter
public enum DataLevel {
    /**
     * 层级定义
     */
    LEVEL2("2", "level2"),
    LEVEL3("3", "level3"),
    LEVEL4("4", "level4");

    private String level;
    private String desc;

    DataLevel(String level, String desc) {
        this.level = level;
        this.desc = desc;
    }

}

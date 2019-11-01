package com.imooc.ad.mysql.enums;

import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * @Author: hekai
 * @Date: 2019-08-07 10:11
 */
public enum OpType {
    /**
     * 四种类型：新增、更新、删除、其他
     */
    ADD,
    UPDATE,
    DELETE,
    OTHER;

    public static OpType to(EventType eventType) {
        switch (eventType) {
            case EXT_WRITE_ROWS:
                return ADD;
            case EXT_UPDATE_ROWS:
                return UPDATE;
            case EXT_DELETE_ROWS:
                return DELETE;
            default:
                return OTHER;
        }
    }
}

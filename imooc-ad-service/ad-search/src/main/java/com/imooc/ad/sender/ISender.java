package com.imooc.ad.sender;

import com.imooc.ad.mysql.dto.MysqlRowData;

/**
 * @Author: hekai
 * @Date: 2019-08-12 14:54
 */
public interface ISender {

    void sender(MysqlRowData rowData);
}

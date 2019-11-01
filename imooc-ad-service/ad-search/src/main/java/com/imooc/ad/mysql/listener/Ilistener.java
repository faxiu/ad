package com.imooc.ad.mysql.listener;

import com.imooc.ad.mysql.dto.BinlogRowData;

/**
 * @Author: hekai
 * @Date: 2019-08-08 17:47
 */
public interface Ilistener {

    void register();

    void onEvent(BinlogRowData eventData);
}

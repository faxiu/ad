package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.imooc.ad.mysql.constant.Constant;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.MysqlRowData;
import com.imooc.ad.mysql.dto.TableTemplate;
import com.imooc.ad.mysql.enums.OpType;
import com.imooc.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hekai
 * @Date: 2019-08-12 14:57
 */
@Slf4j
@Component
public class IncrementListener implements Ilistener {

    @Resource(name = "indexSender")
    private ISender sender;

    private final AggregationListener aggregationListener;

    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }

    @Override
    @PostConstruct
    public void register() {
        log.info("incrementListener register db and table info");
        Constant.table2Db.forEach((k, v) ->
                aggregationListener.register(v, k, this));
    }

    @Override
    public void onEvent(BinlogRowData eventData) {
        TableTemplate tableTemplate = eventData.getTableTemplate();
        EventType eventType = eventData.getEventType();

        //包装成最后需要投递的参数
        MysqlRowData rowData = new MysqlRowData();
        rowData.setTableName(tableTemplate.getTableName());
        rowData.setLevel(tableTemplate.getLevel());
        OpType optype = OpType.to(eventType);
        rowData.setOpType(optype);

        //取出模版中该操作对应的字段列表
        List<String> fieldList = tableTemplate.getOpTypeFieldSetMap().get(optype);
        if (null == fieldList) {
            log.error("{} not support for {}", optype, tableTemplate.getTableName());
        }

        eventData.getAfter().forEach(map -> {
            Map<String, String> afterMap = new HashMap<>();
            map.forEach(afterMap::put);
            rowData.getFieldValueMap().add(afterMap);
        });

        sender.sender(rowData);
    }
}

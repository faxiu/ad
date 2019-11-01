package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.imooc.ad.mysql.TemplateHolder;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: hekai
 * @Date: 2019-08-09 09:39
 * 实现 eventData 到 binlogRowData 的转换
 */
@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String dbName;

    private String tableName;

    private Map<String, Ilistener> listenerMap = new HashMap<>();

    private final TemplateHolder templateHolder;

    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }

    /**
     * 构造key
     *
     * @param dbName
     * @param tableName
     * @return
     */
    private String genKey(String dbName, String tableName) {
        return dbName + ":" + tableName;
    }

    /**
     * 注册listener
     *
     * @param dbName
     * @param tableName
     * @param listener
     */
    public void register(String dbName, String tableName, Ilistener listener) {
        log.info("register: {}-{}", dbName, tableName);
        this.listenerMap.put(genKey(dbName, tableName), listener);
    }

    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();
        log.debug("eventType: {}", eventType);

        if (eventType == EventType.TABLE_MAP) {
            TableMapEventData data = event.getData();
            this.dbName = data.getDatabase();
            this.tableName = data.getTable();
            return;
        }

        //判断是否是增、改、删
        if (eventType != EventType.EXT_WRITE_ROWS && eventType != EventType.EXT_UPDATE_ROWS && eventType != EventType.EXT_DELETE_ROWS) {
            return;
        }
        //判断数据库名和表名是否存在
        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)) {
            log.error("no meta data event");
            return;
        }
        //找到对应表有兴趣的监听器
        String key = genKey(dbName, tableName);
        Ilistener listener = this.listenerMap.get(key);
        if (null == listener) {
            log.debug("skip: {}", key);
            return;
        }

        log.info("trigger event: {}", eventType.name());

        try {
            BinlogRowData binlogRowData = buildRowData(event.getData());
            if (null == binlogRowData) {
                return;
            }
            binlogRowData.setEventType(eventType);
            listener.onEvent(binlogRowData);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            this.dbName = "";
            this.tableName = "";
        }
    }

    /**
     * 统一返回三种类型的改变后的rows
     * @param eventData
     * @return
     */
    private List<Serializable[]> getAfterValues(EventData eventData) {
        if (eventData instanceof WriteRowsEventData) {
            return ((WriteRowsEventData) eventData).getRows();
        }

        if (eventData instanceof UpdateRowsEventData) {
            return ((UpdateRowsEventData) eventData).getRows().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        }

        if (eventData instanceof DeleteRowsEventData) {
            return ((DeleteRowsEventData) eventData).getRows();
        }
        return Collections.emptyList();
    }

    /**
     * 将eventData 转化为 BinlogRowData
     * @param eventData
     * @return
     */
    private BinlogRowData buildRowData(EventData eventData) {

        List<Serializable[]> afterValues = getAfterValues(eventData);

        TableTemplate table = templateHolder.getTable(tableName);
        if(null == table){
            log.warn("table not found: {}", tableName);
            return null;
        }

        List<Map<String, String>> afterMapList = new ArrayList<>();
        for (Serializable[] afterValue : afterValues) {
            Map<String, String> afterMap = new HashMap<>();
            int length = afterValue.length;
            for (int i = 0; i < length; i++) {
                // 取出当前位置对应的列名
                String columnName = table.getPosMap().get(i);
                // 如果没有则说明不关心这个列
                if(null == columnName){
                    log.debug("ignore position: {}", i);
                    continue;
                }
                String columnValue = afterValue[i].toString();
                afterMap.put(columnName, columnValue);
            }
            afterMapList.add(afterMap);
        }
        BinlogRowData binlogRowData = new BinlogRowData();
        binlogRowData.setAfter(afterMapList);
        binlogRowData.setTableTemplate(table);
        return binlogRowData;
    }
}























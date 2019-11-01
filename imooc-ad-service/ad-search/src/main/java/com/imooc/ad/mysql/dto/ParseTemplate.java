package com.imooc.ad.mysql.dto;

import com.imooc.ad.mysql.enums.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @Author: hekai
 * @Date: 2019-08-08 15:15
 */
@Data
public class ParseTemplate {

    private String database;

    private Map<String, TableTemplate> tableTemplateMap = new HashMap<>();

    public static ParseTemplate parse(Template tem) {
        ParseTemplate template = new ParseTemplate();

        template.setDatabase(tem.getDatabase());

        tem.getTableList().forEach(t ->
        {
            String tableName = t.getTableName();
            Integer level = t.getLevel();

            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(tableName);
            tableTemplate.setLevel(level.toString());

            Map<OpType, List<String>> opTypeFieldSetMap = tableTemplate.getOpTypeFieldSetMap();

            t.getInsert().forEach(i ->
                    getAndCreateIfNeed(OpType.ADD, opTypeFieldSetMap, ArrayList::new).add(i.getColumn()));
            t.getUpdate().forEach(u ->
                    getAndCreateIfNeed(OpType.UPDATE, opTypeFieldSetMap, ArrayList::new).add(u.getColumn()));
            t.getDelete().forEach(d ->
                    getAndCreateIfNeed(OpType.DELETE, opTypeFieldSetMap, ArrayList::new).add(d.getColumn()));
            tableTemplate.setOpTypeFieldSetMap(opTypeFieldSetMap);

            template.tableTemplateMap.put(tableName, tableTemplate);
        });
        return template;
    }

    private static <T, R> R getAndCreateIfNeed(T key, Map<T, R> map, Supplier<R> factory) {
        return map.computeIfAbsent(key, r -> factory.get());
    }
}

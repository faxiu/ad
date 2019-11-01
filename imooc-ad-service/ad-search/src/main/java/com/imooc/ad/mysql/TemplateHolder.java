package com.imooc.ad.mysql;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.mysql.dto.ParseTemplate;
import com.imooc.ad.mysql.dto.TableTemplate;
import com.imooc.ad.mysql.dto.Template;
import com.imooc.ad.mysql.enums.OpType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: hekai
 * @Date: 2019-08-08 16:23
 */
@Slf4j
@Component
public class TemplateHolder {

    private ParseTemplate parseTemplate;
    private String sql_schema = "select table_schema,table_name," +
            "column_name,ordinal_position from information_schema.columns " +
            "where table_schema = ? and table_name= ?";

    private final JdbcTemplate jdbcTemplate;

    public TemplateHolder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        loadJson("template.json");
    }

    public TableTemplate getTable(String tableName) {
        return parseTemplate.getTableTemplateMap().get(tableName);
    }

    private void loadJson(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);

        try {
            Template template = null;
            if (inputStream != null) {
                template = JSON.parseObject(inputStream, Charset.defaultCharset(), Template.class);
            }
            this.parseTemplate = ParseTemplate.parse(template);
            loadMeta();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("fail to parse json file");
        }
    }

    //将字段名和索引对应起来,填充TableTemplate的posMap
    private void loadMeta() {
        parseTemplate.getTableTemplateMap().forEach((tableName, tableTemplate) -> {
            List<String> insertFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.ADD);
            List<String> updateFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.UPDATE);
            List<String> deleteFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.DELETE);

            jdbcTemplate.query(sql_schema, new Object[]{parseTemplate.getDatabase(), tableTemplate.getTableName()},
                    (rs, i) -> {
                        int position = rs.getInt("ORDINAL_POSITION");
                        String columnName = rs.getString("COLUMN_NAME");

                        if ((null != insertFields && insertFields.contains(columnName))
                                || (null != updateFields && updateFields.contains(columnName)
                                || (null != deleteFields && deleteFields.contains(columnName)))) {
                            tableTemplate.getPosMap().put(position - 1, columnName);
                        }
                        return null;
                    });
        });
    }
}

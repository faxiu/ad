package com.imooc.ad.mysql.dto;

import com.imooc.ad.mysql.enums.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hekai
 * @Date: 2019-08-08 14:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableTemplate {

    private String tableName;

    private String level;

    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();

    /**
     * 字段的索引到字段名的映射
     */
    private Map<Integer, String> posMap = new HashMap<>();
}

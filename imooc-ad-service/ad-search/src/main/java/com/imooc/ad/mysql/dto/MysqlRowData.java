package com.imooc.ad.mysql.dto;

import com.imooc.ad.mysql.enums.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: hekai
 * @Date: 2019-08-12 14:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MysqlRowData {

    private String tableName;

    private String level;

    private OpType opType;

    //Map<String, String>  列名, 列值
    private List<Map<String, String>> fieldValueMap = new ArrayList<>();
}

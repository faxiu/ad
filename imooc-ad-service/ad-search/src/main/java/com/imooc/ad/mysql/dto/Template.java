package com.imooc.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: hekai
 * @Date: 2019-08-08 14:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {

    private String database;

    private List<JsonTable> tableList;
}

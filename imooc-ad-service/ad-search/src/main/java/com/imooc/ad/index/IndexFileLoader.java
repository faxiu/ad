package com.imooc.ad.index;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.dump.DConstant;
import com.imooc.ad.dump.table.AdCreativeTable;
import com.imooc.ad.dump.table.AdCreativeUnitTable;
import com.imooc.ad.dump.table.AdPlanTable;
import com.imooc.ad.dump.table.AdUnitDistrictTable;
import com.imooc.ad.dump.table.AdUnitItTable;
import com.imooc.ad.dump.table.AdUnitKeywordTable;
import com.imooc.ad.dump.table.AdUnitTable;
import com.imooc.ad.handler.AdLevelDataHandler;
import com.imooc.ad.mysql.enums.OpType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: hekai
 * @Date: 2019-08-07 14:33
 *
 * @DependsOn: 该注解用于声明当前bean依赖于另外一个bean。所依赖的bean会被容器确保在当前bean实例化之前被实例化.
 */
@Component
@DependsOn("dataTable")
public class IndexFileLoader {

    /**
     * 在具体Bean的实例化过程中，@PostConstruct注释的方法，会在构造方法之后，init方法之前进行调用
     */
    @PostConstruct
    public void init() {

        List<String> adPlanStrings = loadDumpData(String.format("%s%s", DConstant.DATA_TOOT_DIR, DConstant.AD_PLAN));
        adPlanStrings.forEach(p ->
                AdLevelDataHandler.handleLevel2(JSON.parseObject(p, AdPlanTable.class), OpType.ADD)
        );

        List<String> adCreativeStrings = loadDumpData(String.format("%s%s", DConstant.DATA_TOOT_DIR, DConstant.AD_CREATIVE));
        adCreativeStrings.forEach(c ->
                AdLevelDataHandler.handleLevel2(JSON.parseObject(c, AdCreativeTable.class), OpType.ADD)
        );

        List<String> adUnitStrings = loadDumpData(String.format("%s%s", DConstant.DATA_TOOT_DIR, DConstant.AD_UNIT));
        adUnitStrings.forEach(u ->
                AdLevelDataHandler.handleLevel3(JSON.parseObject(u, AdUnitTable.class), OpType.ADD)
        );

        List<String> creativeUnitStrings = loadDumpData(String.format("%s%s", DConstant.DATA_TOOT_DIR, DConstant.AD_CREATIVE_UNIT));
        creativeUnitStrings.forEach(c ->
                AdLevelDataHandler.handleLevel3(JSON.parseObject(c, AdCreativeUnitTable.class), OpType.ADD)
        );

        List<String> unitKeywordStrings = loadDumpData(String.format("%s%s", DConstant.DATA_TOOT_DIR, DConstant.AD_UNIT_KEYWORD));
        unitKeywordStrings.forEach(u ->
                AdLevelDataHandler.handleLevel4(JSON.parseObject(u, AdUnitKeywordTable.class), OpType.ADD)
        );


        List<String> unitItStrings = loadDumpData(String.format("%s%s", DConstant.DATA_TOOT_DIR, DConstant.AD_UNIT_IT));
        unitItStrings.forEach(u ->
                AdLevelDataHandler.handleLevel4(JSON.parseObject(u, AdUnitItTable.class), OpType.ADD)
        );

        List<String> unitDistrictStrings = loadDumpData(String.format("%s%s", DConstant.DATA_TOOT_DIR, DConstant.AD_UNIT_DISTRICT));
        unitDistrictStrings.forEach(u ->
                AdLevelDataHandler.handleLevel4(JSON.parseObject(u, AdUnitDistrictTable.class), OpType.ADD)
        );
    }

    private List<String> loadDumpData(String fileName) {

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

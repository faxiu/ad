package com.imooc.ad.handler;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.dump.table.AdCreativeTable;
import com.imooc.ad.dump.table.AdCreativeUnitTable;
import com.imooc.ad.dump.table.AdPlanTable;
import com.imooc.ad.dump.table.AdUnitDistrictTable;
import com.imooc.ad.dump.table.AdUnitItTable;
import com.imooc.ad.dump.table.AdUnitKeywordTable;
import com.imooc.ad.dump.table.AdUnitTable;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.IndexAware;
import com.imooc.ad.index.adplan.AdPlanIndex;
import com.imooc.ad.index.adplan.AdPlanObject;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.index.adunit.AdUnitObject;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.creativeunit.CreativeUnitIndex;
import com.imooc.ad.index.creativeunit.CreativeUnitObject;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.district.UnitDistrictObject;
import com.imooc.ad.index.interest.UnitItIndex;
import com.imooc.ad.index.interest.UnitItObject;
import com.imooc.ad.index.keyword.UnitKeywordIndex;
import com.imooc.ad.index.keyword.UnitKeywordObject;
import com.imooc.ad.mysql.enums.OpType;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: hekai
 * @Date: 2019-08-07 10:09
 * 1.索引之间存在着层级的划分，也就是依赖关系的划分
 * 2.加载全量索引其实是增量索引"添加"的一种特殊实现
 */
@Slf4j
public class AdLevelDataHandler {

    public static void handleLevel2(AdPlanTable planTable, OpType type) {
        AdPlanObject planObject = new AdPlanObject(
                planTable.getPlanId(), planTable.getUserId(), planTable.getPlanStatus(), planTable.getStartTime(), planTable.getEndTime()
        );
        handleBinLogEvent(DataTable.of(AdPlanIndex.class), planObject.getPlanId(), planObject, type);
    }

    public static void handleLevel2(AdCreativeTable creativeTable, OpType type) {
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(), creativeTable.getName(), creativeTable.getType(), creativeTable.getMaterialType(),
                creativeTable.getHeight(), creativeTable.getWidth(), creativeTable.getAuditStatus(), creativeTable.getAdUrl()
        );
        handleBinLogEvent(DataTable.of(CreativeIndex.class), creativeObject.getAdId(), creativeObject, type);
    }

    public static void handleLevel3(AdUnitTable unitTable, OpType type) {
        AdPlanObject planObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if (null == planObject) {
            log.error("handleLevel3 find AdPlanObject error: {}", unitTable.getPlanId());
            return;
        }

        AdUnitObject unitObject = new AdUnitObject(
                unitTable.getUnitId(), unitTable.getUnitStatus(), unitTable.getPositionType(), unitTable.getPlanId(), planObject
        );
        handleBinLogEvent(DataTable.of(AdUnitIndex.class), unitObject.getUnitId(), unitObject, type);
    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("CreativeUnitIndex not support update");
        }

        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        AdUnitObject adUnitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        if (null == creativeObject || null == adUnitObject) {
            log.error("AdCreativeUnitTable index error: {}", JSON.toJSONString(creativeUnitTable));
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(creativeUnitTable.getAdId(), creativeUnitTable.getUnitId());
        handleBinLogEvent(DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(creativeUnitTable.getAdId().toString(), creativeUnitTable.getUnitId().toString()),
                creativeUnitObject,
                type
        );
    }

    public static void handleLevel4(AdUnitKeywordTable unitKeywordTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("keyword can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitKeywordTable.getUnitId());
        if (null == unitObject) {
            log.error("unitKeywordTable index error: {}", unitKeywordTable.getUnitId());
            return;
        }

        UnitKeywordObject unitKeywordObject = new UnitKeywordObject(unitKeywordTable.getUnitId(), unitKeywordTable.getKeyword());

        Set<Long> value = new HashSet<>(Collections.singleton(unitKeywordTable.getUnitId()));
        handleBinLogEvent(DataTable.of(UnitKeywordIndex.class), unitKeywordObject.getKeyword(), value, type);
    }

    public static void handleLevel4(AdUnitItTable unitItTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("it can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if (null == unitObject) {
            log.error("unitKeywordTable index error: {}", unitItTable.getUnitId());
            return;
        }

        UnitItObject unitItObject = new UnitItObject(unitItTable.getUnitId(), unitItTable.getItTag());
        Set<Long> value = new HashSet<>(Collections.singleton(unitItObject.getUnitId()));
        handleBinLogEvent(DataTable.of(UnitItIndex.class), unitItTable.getItTag(), value, type);
    }

    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("district can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if (null == unitObject) {
            log.error("AdUnitDistrictTable index error: {}", unitDistrictTable.getUnitId());
            return;
        }

        UnitDistrictObject unitDistrictObject = new UnitDistrictObject(unitDistrictTable.getUnitId(),
                unitDistrictTable.getProvince(), unitDistrictTable.getCity());
        String key = CommonUtils.stringConcat(unitDistrictObject.getProvince(), unitDistrictObject.getCity());
        Set<Long> value = new HashSet<>(Collections.singleton(unitDistrictObject.getUnitId()));
        handleBinLogEvent(DataTable.of(UnitDistrictIndex.class), key, value, type);
    }

    private static <K, V> void handleBinLogEvent(IndexAware<K, V> index, K key, V value, OpType type) {
        switch (type) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key, value);
                break;
            default:
                break;
        }
    }
}

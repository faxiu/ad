package com.imooc.ad.index.creativeunit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: hekai
 * @Date: 2019-08-06 13:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeUnitObject {

    private Long adId;

    private Long unitId;

    //key: adId-unitId
}

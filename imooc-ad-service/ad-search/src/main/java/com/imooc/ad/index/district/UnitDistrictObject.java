package com.imooc.ad.index.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: hekai
 * @Date: 2019-08-06 10:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDistrictObject {

    private Long unitId;

    private String province;

    private String city;

    //<Long, Set<String>
    // province-city
}

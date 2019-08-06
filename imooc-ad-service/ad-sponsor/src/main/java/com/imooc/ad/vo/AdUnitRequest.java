package com.imooc.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: hekai
 * @Date: 2019-08-01 13:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitRequest {

    private Long planId;

    private String unitName;

    private Integer positionType;

    private Long budget;

    public boolean createValidate() {
        return null != planId && !StringUtils.isBlank(unitName) && positionType != null && budget != null;
    }
}

package com.imooc.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: hekai
 * @Date: 2019-08-05 16:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanTable {

    private Long planId;

    private Long userId;

    private Integer planStatus;

    private Date startTime;

    private Date endTime;

}

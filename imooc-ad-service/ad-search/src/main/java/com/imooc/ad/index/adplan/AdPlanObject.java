package com.imooc.ad.index.adplan;

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
public class AdPlanObject {

    private Long planId;

    private Long userId;

    private Integer planStatus;

    private Date startTime;

    private Date endTime;

    void update(AdPlanObject newObject) {

        if (null != newObject.getPlanId()) {
            this.planId = newObject.getPlanId();
        }
        if (null != newObject.getUserId()) {
            this.userId = newObject.getUserId();
        }
        if (null != newObject.getPlanStatus()) {
            this.planStatus = newObject.getPlanStatus();
        }
        if (null != newObject.getStartTime()) {
            this.startTime = newObject.getStartTime();
        }
        if (null != newObject.getEndTime()) {
            this.endTime = newObject.getEndTime();
        }
    }
}

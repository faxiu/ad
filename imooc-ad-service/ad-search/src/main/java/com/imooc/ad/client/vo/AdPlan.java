package com.imooc.ad.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: hekai
 * @Date: 2019-08-05 14:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlan {

    private Long id;

    private Long userId;

    private String planName;

    private Integer planStatus;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;
}

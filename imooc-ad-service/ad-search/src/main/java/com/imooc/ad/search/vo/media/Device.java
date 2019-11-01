package com.imooc.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: hekai
 * @Date: 2019-08-14 17:08
 * 设备信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    //设备ID
    private String deviceId;

    //mac
    private String mac;

    //ip
    private String ip;

    //机型编码
    private String model;

    //分辨率尺寸
    private String displaySize;

    //屏幕尺寸
    private String screenSize;

    //设备序列号
    private String serialName;
}

package com.imooc.ad.search.vo;

import com.imooc.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: hekai
 * @Date: 2019-08-14 17:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    //Map<String, List<Creative>>  String:广告位编码
    private Map<String, List<Creative>> adSlot2Ads = new HashMap<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative {
        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;

        //展示监测 url
        private List<String> showMonitorUrl = Arrays.asList("www.baidu.com", "wwww.baidu.com");

        //点击监测 url
        private List<String> clickMonitorUrl = Arrays.asList("www.baidu.com", "wwww.baidu.com");
    }

    public static Creative convert(CreativeObject object) {
        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setAdUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());
        return creative;
    }
}

package com.imooc.ad.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.annotation.IgnoreResponseAdvice;
import com.imooc.ad.client.vo.AdPlan;
import com.imooc.ad.client.vo.AdPlanGetRequest;
import com.imooc.ad.client.SponsorClient;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.adplan.AdPlanIndex;
import com.imooc.ad.search.Isearch;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;
import com.imooc.ad.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Author: hekai
 * @Date: 2019-08-05 14:08
 */
@Slf4j
@RestController
public class SearchController {

    private final Isearch search;

    private final RestTemplate restTemplate;

    private final SponsorClient sponsorClient;

    public SearchController(RestTemplate restTemplate, Isearch search, SponsorClient sponsorClient) {
        this.restTemplate = restTemplate;
        this.search = search;
        this.sponsorClient = sponsorClient;
    }

    @PostMapping("/fetchAds")
    public SearchResponse fetchAds(@RequestBody SearchRequest request) {
        log.info("ad-search: fetchAds -> {}", JSON.toJSONString(request));
        return search.fetchAds(request);
    }

    @SuppressWarnings("all")
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlansByRibbon")
    public CommonResponse<List<AdPlan>> getAdPlansByRibbon(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlansByRibbon - {}", JSON.toJSONString(request));
        return restTemplate.postForEntity("http://eureka-client-ad-sponsor/ad-sponsor/adPlan/get",
                request, CommonResponse.class)
                .getBody();
    }

    @PostMapping("/getAdPlans")
    @IgnoreResponseAdvice
    public CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlans - {}", JSON.toJSONString(request));
        return sponsorClient.getAdPlans(request);
    }

}

package com.imooc.ad.service;

import com.imooc.ad.Application;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.vo.AdPlanGetRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

/**
 * @Author: hekai
 * @Date: 2019-09-06 11:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdPlanServiceTest {

    @Autowired
    private IAdPlanService adPlanService;

    @Test
    public void getAdPlanByIds() {
        AdPlanGetRequest request = new AdPlanGetRequest(15L, Collections.singletonList(10L));
        try {
            List<AdPlan> adPlanList = adPlanService.getAdPlanByIds(request);
            System.out.println(adPlanList);
            Assert.assertTrue(adPlanList.size() > 0);
        } catch (AdException e) {
            e.printStackTrace();
        }
    }
}

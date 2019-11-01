package com.imooc.ad.search.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.index.CommonStatus;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.index.adunit.AdUnitObject;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.creativeunit.CreativeUnitIndex;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interest.UnitItIndex;
import com.imooc.ad.index.keyword.UnitKeywordIndex;
import com.imooc.ad.search.Isearch;
import com.imooc.ad.search.feature.DistrictFeature;
import com.imooc.ad.search.feature.FeatureRelation;
import com.imooc.ad.search.feature.ItFeature;
import com.imooc.ad.search.feature.KeywordFeature;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;
import com.imooc.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @Author: hekai
 * @Date: 2019-09-02 10:30
 */
@Slf4j
@Service
public class SearchImpl implements Isearch {
    @Override
    public SearchResponse fetchAds(SearchRequest request) {

        //请求广告位的信息
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();

        //三个feature
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        ItFeature itFeature = request.getFeatureInfo().getItFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        FeatureRelation relation = request.getFeatureInfo().getRelation();

        //构造响应对象
        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();

        for (AdSlot adSlot : adSlots) {
            Set<Long> targetUnitIdSet;

            //根据流量类型获取初始 AdUnit
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());

            if (relation == FeatureRelation.AND) {
                filterKeyWordFeature(adUnitIdSet, keywordFeature);
                filterItFeature(adUnitIdSet, itFeature);
                filterDistrictFeature(adUnitIdSet, districtFeature);

                targetUnitIdSet = adUnitIdSet;
            } else {
                targetUnitIdSet = getORRelationUnitIds(adUnitIdSet, keywordFeature, itFeature, districtFeature);
            }

            List<AdUnitObject> unitObjects = DataTable.of(AdUnitIndex.class).fetch(targetUnitIdSet);

            filterAdUnitAndPlanStatus(unitObjects, CommonStatus.VALID);

            List<Long> adIds = DataTable.of(CreativeUnitIndex.class).selectAds(unitObjects);
            List<CreativeObject> creatives = DataTable.of(CreativeIndex.class).fetch(adIds);
            //通过AdSlot 实现对 CreativeObject 的过滤
            filterCreativeByAdSlot(creatives, adSlot.getWidth(), adSlot.getHeight(), adSlot.getType());

            adSlot2Ads.put(adSlot.getAdSlotCode(), buildCreativeResponse(creatives));
        }
        log.info("fetchAds: {} - {}", JSON.toJSONString(request), JSON.toJSONString(response));
        return response;
    }

    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet, KeywordFeature keywordFeature,
                                           ItFeature itFeature, DistrictFeature districtFeature) {
        if (CollectionUtils.isEmpty(adUnitIdSet)) {
            return Collections.emptySet();
        }

        Set<Long> keywordUintIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);

        filterKeyWordFeature(keywordUintIdSet, keywordFeature);
        filterItFeature(itUnitIdSet, itFeature);
        filterDistrictFeature(districtUnitIdSet, districtFeature);

        return new HashSet<>(CollectionUtils.union(CollectionUtils.union(keywordUintIdSet, itUnitIdSet), districtUnitIdSet));
    }

    private void filterKeyWordFeature(Collection<Long> adUnitIds, KeywordFeature keywordFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())) {
            CollectionUtils.filter(adUnitIds,
                    adUnitId -> DataTable.of(UnitKeywordIndex.class).match(adUnitId, keywordFeature.getKeywords()));
        }
    }

    private void filterItFeature(Collection<Long> adUnitIds, ItFeature itFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(itFeature.getIts())) {
            CollectionUtils.filter(adUnitIds,
                    adUnitId -> DataTable.of(UnitItIndex.class).match(adUnitId, itFeature.getIts()));
        }
    }

    private void filterDistrictFeature(Collection<Long> adUnitIds, DistrictFeature districtFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())) {
            CollectionUtils.filter(adUnitIds,
                    adUnitId -> DataTable.of(UnitDistrictIndex.class).match(adUnitId, districtFeature.getDistricts()));
        }
    }

    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects, CommonStatus status) {
        if (CollectionUtils.isEmpty(unitObjects)) {
            return;
        }
        CollectionUtils.filter(unitObjects,
                unitObject -> unitObject.getUnitStatus().equals(status.getStatus())
                        && unitObject.getAdPlanObject().getPlanStatus().equals(status.getStatus())
        );
    }

    private void filterCreativeByAdSlot(List<CreativeObject> creatives, Integer width, Integer height, List<Integer> type) {
        if (CollectionUtils.isEmpty(creatives)) {
            return;
        }

        CollectionUtils.filter(creatives,
                creative -> creative.getAuditStatus().equals(CommonStatus.VALID.getStatus())
                        && creative.getWidth().equals(width)
                        && creative.getHeight().equals(height)
                        && type.contains(creative.getType())
        );
    }

    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creatives) {
        if (CollectionUtils.isEmpty(creatives)) {
            return Collections.emptyList();
        }

        CreativeObject randomObject = creatives.get(Math.abs(new Random().nextInt()) % creatives.size());

        return Collections.singletonList(
                SearchResponse.convert(randomObject)
        );
    }
}

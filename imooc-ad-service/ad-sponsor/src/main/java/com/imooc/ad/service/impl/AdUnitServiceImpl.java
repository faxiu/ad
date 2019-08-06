package com.imooc.ad.service.impl;

import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdPlanRepository;
import com.imooc.ad.dao.AdUnitRepository;
import com.imooc.ad.dao.CreativeRepository;
import com.imooc.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.imooc.ad.dao.unit_condition.AdUnitItRepository;
import com.imooc.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.imooc.ad.dao.unit_condition.CreativeUnitRepository;
import com.imooc.ad.entity.AdUnit;
import com.imooc.ad.entity.unit_condition.AdUnitDistrict;
import com.imooc.ad.entity.unit_condition.AdUnitIt;
import com.imooc.ad.entity.unit_condition.AdUnitKeyword;
import com.imooc.ad.entity.unit_condition.CreativeUnit;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.IAdUnitService;
import com.imooc.ad.vo.AdUnitDistrictRequest;
import com.imooc.ad.vo.AdUnitDistrictResponse;
import com.imooc.ad.vo.AdUnitItRequest;
import com.imooc.ad.vo.AdUnitItResponse;
import com.imooc.ad.vo.AdUnitKeywordRequest;
import com.imooc.ad.vo.AdUnitKeywordResponse;
import com.imooc.ad.vo.AdUnitRequest;
import com.imooc.ad.vo.AdUnitResponse;
import com.imooc.ad.vo.CreativeUnitRequest;
import com.imooc.ad.vo.CreativeUnitResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: hekai
 * @Date: 2019-08-01 13:42
 */
@Slf4j
@Service
public class AdUnitServiceImpl implements IAdUnitService {
    private final CreativeRepository creativeRepository;
    private final CreativeUnitRepository creativeUnitRepository;
    private final AdUnitKeywordRepository unitKeywordRepository;
    private final AdUnitItRepository unitItRepository;
    private final AdUnitDistrictRepository unitDistrictRepository;
    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;

    public AdUnitServiceImpl(AdPlanRepository planRepository, AdUnitRepository unitRepository,
                             AdUnitKeywordRepository unitKeywordRepository, AdUnitItRepository unitItRepository,
                             AdUnitDistrictRepository unitDistrictRepository, CreativeRepository creativeRepository,
                             CreativeUnitRepository creativeUnitRepository) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.unitKeywordRepository = unitKeywordRepository;
        this.unitItRepository = unitItRepository;
        this.unitDistrictRepository = unitDistrictRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }

    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if (!request.createValidate()) {
            throw new AdException(Constants.ErrorMeg.REQUEST_PARAM_ERROR);
        }

        planRepository.findById(request.getPlanId()).orElseThrow(() -> new AdException(Constants.ErrorMeg.CAN_NOT_FIND_RECORD));

        AdUnit oldAdUnit = unitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (oldAdUnit != null) {
            throw new AdException(Constants.ErrorMeg.SAME_NAME_UNIT_ERROR);
        }

        AdUnit newAdUnit = unitRepository.save(new AdUnit(
                request.getPlanId(), request.getUnitName(), request.getPositionType(), request.getBudget()
        ));
        return new AdUnitResponse(newAdUnit.getId(), newAdUnit.getUnitName());
    }

    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        List<Long> unitIds = request.getUnitKeywords().stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMeg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())) {
            request.getUnitKeywords().forEach(keyword ->
                    unitKeywords.add(new AdUnitKeyword(keyword.getUnitId(), keyword.getKeyword()))
            );
            ids = unitKeywordRepository.saveAll(unitKeywords).stream().map(
                    AdUnitKeyword::getId
            ).collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitIts().stream().map(
                AdUnitItRequest.UnitIt::getUnitId
        ).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMeg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitIt> unitIts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitIts())) {
            request.getUnitIts().forEach(it ->
                    unitIts.add(new AdUnitIt(it.getUnitId(), it.getItTag()))
            );
            ids = unitItRepository.saveAll(unitIts).stream().map(
                    AdUnitIt::getId
            ).collect(Collectors.toList());
        }
        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIds = request.getUnitDistricts().stream().map(
                AdUnitDistrictRequest.UnitDistrict::getUnitId
        ).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMeg.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitDistrict> unitDistricts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitDistricts())) {
            request.getUnitDistricts().forEach(district ->
                    unitDistricts.add(new AdUnitDistrict(district.getUnitId(), district.getProvince(), district.getCity()))
            );
            ids = unitDistrictRepository.saveAll(unitDistricts).stream().map(
                    AdUnitDistrict::getId
            ).collect(Collectors.toList());
        }
        return new AdUnitDistrictResponse(ids);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
        List<Long> unitIds = request.getUnitItems().stream().map(
                CreativeUnitRequest.CreativeUnitItem::getUnitId
        ).collect(Collectors.toList());
        List<Long> creativeIds = request.getUnitItems().stream().map(
                CreativeUnitRequest.CreativeUnitItem::getCreativeId
        ).collect(Collectors.toList());
        if (!(isRelatedUnitExist(unitIds) && isRelatedCreativeExist(creativeIds))) {
            throw new AdException(Constants.ErrorMeg.REQUEST_PARAM_ERROR);
        }
        List<CreativeUnit> creativeUnits = new ArrayList<>();
        request.getUnitItems().forEach(item ->
                creativeUnits.add(new CreativeUnit(item.getCreativeId(), item.getUnitId()))
        );
        List<Long> ids = creativeUnitRepository.saveAll(creativeUnits).stream().map(
                CreativeUnit::getId
        ).collect(Collectors.toList());
        return new CreativeUnitResponse(ids);
    }

    private boolean isRelatedUnitExist(List<Long> unitIds) {
        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }
        return unitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }

    private boolean isRelatedCreativeExist(List<Long> creativeIds) {
        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }
        return creativeRepository.findAllById(creativeIds).size() == new HashSet<>(creativeIds).size();
    }
}

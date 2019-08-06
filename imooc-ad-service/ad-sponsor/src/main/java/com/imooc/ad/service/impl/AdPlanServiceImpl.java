package com.imooc.ad.service.impl;

import com.imooc.ad.constant.CommonStatus;
import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdPlanRepository;
import com.imooc.ad.dao.AdUserRepository;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUser;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.IAdPlanService;
import com.imooc.ad.utils.CommonUtils;
import com.imooc.ad.vo.AdPlanGetRequest;
import com.imooc.ad.vo.AdPlanRequest;
import com.imooc.ad.vo.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: hekai
 * @Date: 2019-08-01 10:58
 */
@Slf4j
@Service
public class AdPlanServiceImpl implements IAdPlanService {

    private final AdUserRepository userRepository;
    private final AdPlanRepository planRepository;

    public AdPlanServiceImpl(AdUserRepository userRepository, AdPlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Override
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {
        if (!request.createValidate()) {
            throw new AdException(Constants.ErrorMeg.REQUEST_PARAM_ERROR);
        }

        //确保关联的user对象存在
        Optional<AdUser> adUser = userRepository.findById(request.getUserId());
        if (!adUser.isPresent()) {
            throw new AdException(Constants.ErrorMeg.CAN_NOT_FIND_RECORD);
        }

        AdPlan oldAdPlan = planRepository.findByUserIdAndPlanName(request.getUserId(), request.getPlanName());
        if (oldAdPlan != null) {
            throw new AdException(Constants.ErrorMeg.SAME_NAME_PLAN_ERROR);
        }
        AdPlan newAdPlan = planRepository.save(new AdPlan(
                request.getUserId(), request.getPlanName(),
                CommonUtils.parseStringDate(request.getStartDate()), CommonUtils.parseStringDate(request.getEndDate())
        ));
        return new AdPlanResponse(newAdPlan.getId(), newAdPlan.getPlanName());
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {
        if (!request.validate()) {
            throw new AdException(Constants.ErrorMeg.REQUEST_PARAM_ERROR);
        }

        return planRepository.findAllByIdInAndUserId(request.getIds(), request.getUserId());
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {
        if (!request.updateValidate()) {
            throw new AdException(Constants.ErrorMeg.REQUEST_PARAM_ERROR);
        }
        AdPlan adPlan = planRepository.findByIdAndUserId(request.getId(), request.getUserId())
                .orElseThrow(() -> new AdException(Constants.ErrorMeg.CAN_NOT_FIND_RECORD));
        if (request.getPlanName() != null) {
            adPlan.setPlanName(request.getPlanName());
        }
        if (request.getStartDate() != null) {
            adPlan.setStartDate(CommonUtils.parseStringDate(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            adPlan.setEndDate(CommonUtils.parseStringDate(request.getEndDate()));
        }
        adPlan.setUpdateTime(new Date());
        adPlan = planRepository.save(adPlan);
        return new AdPlanResponse(adPlan.getId(), adPlan.getPlanName());
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdException {
        if (!request.deleteValidate()) {
            throw new AdException(Constants.ErrorMeg.REQUEST_PARAM_ERROR);
        }
        AdPlan adPlan = planRepository.findByIdAndUserId(request.getId(), request.getUserId())
                .orElseThrow(() -> new AdException(Constants.ErrorMeg.CAN_NOT_FIND_RECORD));
        adPlan.setPlanStatus(CommonStatus.INVALID.getStatus());
        adPlan.setUpdateTime(new Date());
        planRepository.save(adPlan);
    }
}

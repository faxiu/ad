package com.imooc.ad.service.impl;

import com.imooc.ad.dao.CreativeRepository;
import com.imooc.ad.entity.Creative;
import com.imooc.ad.service.ICreativeService;
import com.imooc.ad.vo.CreativeRequest;
import com.imooc.ad.vo.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: hekai
 * @Date: 2019-08-01 17:35
 */
@Slf4j
@Service
public class CreativeServiceImpl implements ICreativeService {

    private final CreativeRepository creativeRepository;

    public CreativeServiceImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public CreativeResponse createCreative(CreativeRequest request) {

        //TODO 校验工作

        Creative creative = creativeRepository.save(request.convertToCreative());
        return new CreativeResponse(creative.getId(), creative.getName());
    }
}

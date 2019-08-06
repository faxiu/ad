package com.imooc.ad.service;

import com.imooc.ad.vo.CreativeRequest;
import com.imooc.ad.vo.CreativeResponse;

/**
 * @Author: hekai
 * @Date: 2019-08-01 16:02
 */
public interface ICreativeService {

    CreativeResponse createCreative(CreativeRequest request);
}

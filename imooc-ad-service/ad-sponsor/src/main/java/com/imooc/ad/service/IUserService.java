package com.imooc.ad.service;

import com.imooc.ad.exception.AdException;
import com.imooc.ad.vo.CreateUserRequest;
import com.imooc.ad.vo.CreateUserResponse;

/**
 * @Author: hekai
 * @Date: 2019-08-01 09:32
 */
public interface IUserService {

    CreateUserResponse createUser(CreateUserRequest request) throws AdException;
}

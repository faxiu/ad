package com.imooc.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: hekai
 * @Date: 2019-08-01 09:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {

    private Long id;

    private String username;

    private String token;

    private Date createTime;

    private Date updateTime;

}

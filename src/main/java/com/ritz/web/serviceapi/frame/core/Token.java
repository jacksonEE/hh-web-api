package com.ritz.web.serviceapi.frame.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Token implements Serializable {

    /**
     * 登录id
     */
    private Integer userId;

    /**
     * @see  UserType
     */
    private UserType userType;

    /**
     * token 的生成时间
     */
    private Date loginTime;
}

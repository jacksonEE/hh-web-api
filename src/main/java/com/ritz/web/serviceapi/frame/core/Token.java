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

    private Integer userId;

    private UserType userType;

    private Date loginTime;
}

package com.ritz.web.serviceapi.vo.movie;

import lombok.Data;

import java.util.List;

/**
 * created by Jackson at 2019/4/23 11:47
 **/
@Data
public class CastsVO {

    private String alt;
    private String name;
    private String id;
    private List<AvatarsVO> avatars;
}

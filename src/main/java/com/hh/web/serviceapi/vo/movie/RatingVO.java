package com.hh.web.serviceapi.vo.movie;

import lombok.Data;

/**
 * created by Jackson at 2019/4/23 11:46
 **/
@Data
public class RatingVO {
    private Integer max;
    private Integer stars;
    private Integer min;
    private Double average;
}

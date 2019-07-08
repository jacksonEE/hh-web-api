package com.hh.web.serviceapi.vo.movie;

import com.hh.web.serviceapi.vo.PagingVO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * created by Jackson at 2019/4/26 11:58
 **/
@Getter
@Setter
public class MovieQuery extends PagingVO {


    private String title;


    // 1-收藏 2-看过 3-最想看
    private Sort sort;

    public enum Sort {
        COLLECT,
        REVIEWS,
        WISH
    }
}

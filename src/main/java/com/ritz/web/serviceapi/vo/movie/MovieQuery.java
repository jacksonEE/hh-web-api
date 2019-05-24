package com.ritz.web.serviceapi.vo.movie;

import com.ritz.web.serviceapi.vo.PagingVO;
import lombok.Data;

/**
 * created by Jackson at 2019/4/26 11:58
 **/
@Data
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

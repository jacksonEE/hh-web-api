package com.hh.web.serviceapi.vo.movie;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * created by Jackson at 2019/4/23 11:46
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieVO {

    private RatingVO rating;
    private List<String> genres;
    private List<CastsVO> casts;
    private Integer collectCount;
    private String originalTitle;
    private String subtype;
    private List<DirectorsVO> directors;
    private Integer year;
    private AvatarsVO images;
    private Integer id;
    private String alt;
    private List<String> countries;
    private Integer reviewsCount;
    private String summary;
    private String title;
    private Integer wishCount;
}

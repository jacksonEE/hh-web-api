package com.hh.web.serviceapi.entities;

import com.hh.web.serviceapi.base.BaseAnnotation;
import com.hh.web.serviceapi.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Analyzer(impl = SmartChineseAnalyzer.class)
@Indexed
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Accessors(chain = true)
@Setter
@BaseAnnotation
@Entity
@Table(name = "movie")
public class Movie extends BaseEntity {

    /**
     * alt
     */
    @Column(columnDefinition = "varchar(255) default ''")
    private String alt;

    /**
     * casts
     */
    @Column(columnDefinition = "text")
    private String casts;

    /**
     * collectCount
     */
    @Column(columnDefinition = "int(10) default 0")
    private Integer collectCount;

    /**
     * directors
     */
    @Column(columnDefinition = "text")
    private String directors;

    /**
     * genres
     */
    @Column(columnDefinition = "varchar(255) default ''")
    private String genres;

    /**
     * images
     */
    @Column(columnDefinition = "text")
    private String images;

    /**
     * movie ID
     */
    @Column(nullable = false, columnDefinition = "int(11) default 0")
    private Integer movieId;

    /**
     * title
     */
    @Field(termVector = TermVector.YES)
    @Column(columnDefinition = "varchar(255) default ''")
    private String title;

    /**
     * original title
     */
    @Column(columnDefinition = "varchar(255) default ''")
    private String originalTitle;

    /**
     * rating
     */
    @Column(columnDefinition = "varchar(255) default ''")
    private String rating;

    /**
     * subtype
     */
    @Column(columnDefinition = "varchar(255) default ''")
    private String subtype;

    /**
     * year
     */
    @Column(columnDefinition = "int(5) default 1970")
    private Integer year;

    /**
     * countries
     */
    @Column(columnDefinition = "varchar(255) default ''")
    private String countries;

    /**
     * reviews count
     */
    @Column(name = "reviewsCount", columnDefinition = "int(11) default 0")
    private Integer reviewsCount;

    /**
     * wish count
     */
    @Column(name = "wishCount", columnDefinition = "int(11) default 0")
    private Integer wishCount;

    /**
     * summary
     */
    @Field(termVector = TermVector.YES)
    @Column(columnDefinition = "text")
    private String summary;
}

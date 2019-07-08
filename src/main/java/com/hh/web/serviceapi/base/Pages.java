package com.hh.web.serviceapi.base;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * created by Jackson at 2019/5/24 11:43
 **/
@Data
@ToString
public class Pages {

    private int index;

    private int size;

    private List content;

    private long total;

    private int totalPages;
}

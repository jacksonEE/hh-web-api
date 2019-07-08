package com.hh.web.serviceapi.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
public class PagingVO {

    @NotNull(message = "index不能为空")
    @Min(value = 0, message = "index不能少于0")
    protected Integer index;

    @NotNull(message = "size不能为空")
    @Min(value = 1, message = "size不能少于1")
    protected Integer size;
}

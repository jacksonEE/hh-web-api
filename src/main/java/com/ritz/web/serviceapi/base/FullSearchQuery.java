package com.ritz.web.serviceapi.base;

import lombok.Data;

import java.util.Objects;

/**
 * created by Jackson at 2019/5/24 14:02
 **/
@Data
public class FullSearchQuery {

    private Class queryCls;

    private String[] fields;

    private int index;

    private int size;

    private String value;


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        FullSearchQuery fsq = new FullSearchQuery();

        public Builder entity(Class cls) {
            fsq.setQueryCls(cls);
            return this;
        }

        public Builder fields(String... fields) {
            fsq.setFields(fields);
            return this;
        }

        public Builder index(int index) {
            fsq.setIndex(index);
            return this;
        }

        public Builder size(int size) {
            fsq.setSize(size);
            return this;
        }

        public Builder value(String value) {
            fsq.setValue(value);
            return this;
        }

        public FullSearchQuery build() {
            Objects.requireNonNull(fsq.getQueryCls(), "FullSearchQuery.entity is null");
            if (fsq.getFields() == null || fsq.getFields().length == 0) {
                throw new NullPointerException("FullSearchQuery.fields is null or empty");
            }
            return fsq;
        }
    }
}

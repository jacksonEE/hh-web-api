package com.hh.web.serviceapi.base.spec;

/**
 * Created by jackson on 2018/7/3.
 */
class SimpleTerm extends Term {

    SimpleTerm(String attrName, Operator operator,boolean and) {
        super(attrName, operator,and);
    }

    SimpleTerm(String attrName, Operator operator, Object value, boolean and) {
        super(attrName, operator, value, and);
    }

    SimpleTerm(String attrName, Operator operator, Object a, Object b, boolean and) {
        super(attrName, operator, a, b, and);
    }
}

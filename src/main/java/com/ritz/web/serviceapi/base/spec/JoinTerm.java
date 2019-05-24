package com.ritz.web.serviceapi.base.spec;

import java.util.LinkedList;

/**
 * Created by jackson on 2018/7/3.
 */
 class JoinTerm extends Term {

    // join 的拼接条件
    private LinkedList<Term> simpleTerms = new LinkedList<>();

    JoinTerm(String attrName, Operator operator,boolean and) {
        super(attrName, operator,and);
    }

    JoinTerm(String attrName, Operator operator) {
        super(attrName, operator);
    }

    void addSimpleTerm(Term simpleTerm) {
        this.simpleTerms.add(simpleTerm);
    }

    LinkedList<Term> getSimpleTerms() {
        return this.simpleTerms;
    }
}

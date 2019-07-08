package com.hh.web.serviceapi.base.spec;


import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Created by jackson on 2018/7/3.
 */
public abstract class Term {

    protected Operator operator;
    protected String attrName;
    protected Object value;
    protected boolean and;
    protected Object[] betweenValue;

    protected Term(String attrName, Operator operator) {
        this.attrName = attrName;
        this.operator = operator;
    }

    protected Term(String attrName, Operator operator, boolean and) {
        this.attrName = attrName;
        this.operator = operator;
        this.and = and;
    }

    protected Term(String attrName, Operator operator, Object value, boolean and) {
        this.attrName = attrName;
        this.operator = operator;
        this.value = value;
        this.and = and;
    }

    protected Term(String attrName, Operator operator, Object a, Object b, boolean and) {
        this.attrName = attrName;
        this.operator = operator;
        this.betweenValue = new Object[]{a, b};
        this.and = and;
    }

    /**
     * EQ 相等
     *
     * @param attrName 属性名
     * @param value    值
     * @return term
     */
    public static Term eq(String attrName, Object value, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.eq attrName is not be null!");
        }

        if (checkNull(value)) {
            throw new NullPointerException("Term.eq value is not be null!");
        }
        return new SimpleTerm(attrName, Operator.EQ, value, and);
    }

    /**
     * NE 不相等
     *
     * @param attrName 属性名
     * @param value    值
     * @return term
     */
    public static Term ne(String attrName, Object value, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.ne attrName is not be null!");
        }

        if (checkNull(value)) {
            throw new NullPointerException("Term.ne value is not be null!");
        }
        return new SimpleTerm(attrName, Operator.NE, value, and);
    }

    /**
     * like 相似
     *
     * @param attrName 属性名
     * @param value    值
     * @return term
     */
    public static Term like(String attrName, Object value, boolean and, Boolean likeSymbol) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.like attrName is not be null!");
        }

        if (checkNull(value)) {
            throw new NullPointerException("Term.like value is not be null!");
        }
        if (likeSymbol != null) {
            value = likeSymbol ? value + "%" : "%" + value;
        }
        return new SimpleTerm(attrName, Operator.LIKE, value, and);
    }

    /**
     * gt 大于
     *
     * @param attrName 属性名
     * @param value    值
     * @return term
     */
    public static Term gt(String attrName, Object value, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.gt attrName is not be null!");
        }

        if (checkNull(value)) {
            throw new NullPointerException("Term.gt value is not be null!");
        }
        return new SimpleTerm(attrName, Operator.GT, value, and);
    }

    /**
     * gte 大于或等于
     *
     * @param attrName 属性名
     * @param value    值
     * @return term
     */
    public static Term gte(String attrName, Object value, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.gte attrName is not be null!");
        }

        if (checkNull(value)) {
            throw new NullPointerException("Term.gte value is not be null!");
        }
        return new SimpleTerm(attrName, Operator.GTE, value, and);
    }

    /**
     * lt 小于
     *
     * @param attrName 属性名
     * @param value    值
     * @return term
     */
    public static Term lt(String attrName, Object value, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.lt attrName is not be null!");
        }

        if (checkNull(value)) {
            throw new NullPointerException("Term.lt value is not be null!");
        }
        return new SimpleTerm(attrName, Operator.LT, value, and);
    }

    /**
     * lte 小于或等于
     *
     * @param attrName 属性名
     * @param value    值
     * @return term
     */
    public static Term lte(String attrName, Object value, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.lte attrName is not be null!");
        }

        if (checkNull(value)) {
            throw new NullPointerException("Term.lte value is not be null!");
        }
        return new SimpleTerm(attrName, Operator.LTE, value, and);
    }

    /**
     * @param attrName 属性名
     * @param a        左区间
     * @param b        右区间
     * @return term
     */
    public static Term between(String attrName, Object a, Object b, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.between attrName is not be null!");
        }

        if (checkNull(a)) {
            throw new NullPointerException("Term.between a is not be null!");
        }

        if (checkNull(b)) {
            throw new NullPointerException("Term.between b is not be null!");
        }
        return new SimpleTerm(attrName, Operator.BETWEEN, a, b, and);
    }

    /**
     * in
     *
     * @param attrName 属性名
     * @param value    值
     * @return term
     */
    public static Term in(String attrName, Collection value, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.in attrName is not be null!");
        }

        if (checkNull(value)) {
            throw new NullPointerException("Term.in value is not be null!");
        }

        if (value.isEmpty()) {
            throw new NullPointerException("Term.in value is not be empty!");
        }
        return new SimpleTerm(attrName, Operator.IN, value, and);
    }

    /**
     * null
     *
     * @param attrName 属性名
     * @return term
     */
    public static Term isNull(String attrName, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.isNull attrName is not be null!");
        }
        return new SimpleTerm(attrName, Operator.NULL, and);
    }


    /**
     * not null
     *
     * @param attrName 属性名
     * @return term
     */
    public static Term notNull(String attrName, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.notNull attrName is not be null!");
        }
        return new SimpleTerm(attrName, Operator.NOT_NULL, and);
    }

    /**
     * empty
     *
     * @param attrName 属性名
     * @return term
     */
    public static Term empty(String attrName, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.empty attrName is not be null!");
        }
        return new SimpleTerm(attrName, Operator.EMPTY, and);
    }

    /**
     * not empty
     *
     * @param attrName 属性名
     * @return term
     */
    public static Term notEmpty(String attrName, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.notEmpty attrName is not be null!");
        }
        return new SimpleTerm(attrName, Operator.NOT_EMPTY, and);
    }

    /**
     * true
     *
     * @param attrName 属性名
     * @return term
     */
    public static Term isTrue(String attrName, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.isTrue attrName is not be null!");
        }
        return new SimpleTerm(attrName, Operator.TRUE, and);
    }

    /**
     * false
     *
     * @param attrName 属性名
     * @return term
     */
    public static Term isFalse(String attrName, boolean and) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.isFalse attrName is not be null!");
        }
        return new SimpleTerm(attrName, Operator.FALSE, and);
    }


    /**
     * left join
     *
     * @param attrName 属性名
     * @return term
     */
    public static Term leftJoin(String attrName) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.leftJoin attrName is not be null!");
        }
        return new JoinTerm(attrName, Operator.LEFT_JOIN);
    }

    /**
     * right join
     *
     * @param attrName 属性名
     * @return term
     */
    public static Term rightJoin(String attrName) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.rightJoin attrName is not be null!");
        }
        return new JoinTerm(attrName, Operator.RIGHT_JOIN);
    }

    /**
     * frame join
     *
     * @param attrName 属性名
     * @return term
     */
    public static Term innerJoin(String attrName) {
        if (checkNull(attrName)) {
            throw new NullPointerException("Term.innerJoin attrName is not be null!");
        }
        return new JoinTerm(attrName, Operator.INNER_JOIN);
    }

    private static boolean checkNull(String attr) {
        return StringUtils.isBlank(attr);
    }

    private static boolean checkNull(Object value) {
        return value == null;
    }

}

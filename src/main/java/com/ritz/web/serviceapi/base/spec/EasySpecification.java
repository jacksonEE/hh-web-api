package com.ritz.web.serviceapi.base.spec;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * 有时间再重写这个鬼东西
 * Created by jackson on 2018/7/3.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class EasySpecification<T> implements Specification<T> {

    private ArrayList<Term> andTerms;
    private ArrayList<Term> orTerms;
    private LinkedList<Term> joinTerms;
    private LinkedHashMap<String, Boolean> sortMap;
    // join连接的第一个条件
    private boolean firstJoin;
    // distinct
    private boolean distinct;
    // 缓存join,join对象复用
    private Map<String, Path> cacheMap = new HashMap<>();

    private EasySpecification(Builder builder) {
        this.andTerms = builder.andTerms;
        this.orTerms = builder.orTerms;
        this.joinTerms = builder.joinTerms;
        this.sortMap = builder.sortMap;
        this.distinct = builder.distinct;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (andTerms == null && orTerms == null && joinTerms == null && (sortMap == null || sortMap.isEmpty())) {
            return query.getRestriction();
        }
        ArrayList<Predicate> predicates = new ArrayList<>();
        if (andTerms != null) {
            predicates.add(handleAnd(root, cb));
        }

        if (orTerms != null) {
            predicates.add(handleOr(root, cb));
        }

        Predicate joinPredicate = null;
        if (joinTerms != null) {
            joinPredicate = handleJoin(root, cb);
        }

        List<Order> orders = null;
        if (sortMap != null) {
            orders = handleOrder(root);
        }

        Predicate resultPredicate = query.distinct(this.distinct)
            .where(predicates.toArray(new Predicate[0])).getRestriction();
        if (joinPredicate != null) {
            resultPredicate = firstJoin ? cb.and(resultPredicate, joinPredicate) : cb.or(resultPredicate, joinPredicate);
        }
        return orders != null ? query.distinct(this.distinct).where(resultPredicate).orderBy(orders).getRestriction() : resultPredicate;
    }

    /**
     * and 条件拼接
     */
    private Predicate handleAnd(Root<T> root, CriteriaBuilder cb) {
        final Predicate[] predicate = new Predicate[1];
        this.andTerms.forEach(term -> predicate[0] = predicate[0] == null
            ? cb.and(just(root, cb, term)) : cb.and(just(root, cb, term), predicate[0]));
        return predicate[0];
    }

    /**
     * or 条件拼接
     */
    private Predicate handleOr(Root<T> root, CriteriaBuilder cb) {
        final Predicate[] predicate = new Predicate[1];
        this.orTerms.forEach(term -> predicate[0] = predicate[0] == null
            ? cb.or(just(root, cb, term)) : cb.or(just(root, cb, term), predicate[0]));
        return predicate[0];
    }

    /**
     * join条件拼接
     */
    private Predicate handleJoin(Root<T> root, CriteriaBuilder cb) {
        final Predicate[] predicate = new Predicate[1];
        this.joinTerms.forEach(joinTerm -> {
            Path suitablePath = getSuitablePath(root, (JoinTerm) joinTerm);
            Predicate p = justJoin(suitablePath, cb, joinTerm);
            if (predicate[0] == null) {
                firstJoin = joinTerm.and;
                predicate[0] = joinTerm.and ? cb.and(p) : cb.or(p);
            } else {
                predicate[0] = joinTerm.and ? cb.and(predicate[0], p) : cb.or(predicate[0], p);
            }
        });
        return predicate[0];
    }

    private List<Order> handleOrder(Root<T> root) {
        ArrayList<Order> orders = new ArrayList<>();
        /*Set<Map.Entry<String, Boolean>> entries = sortMap.entrySet();
        entries.forEach(entry -> {
            String key = entry.getKey();
            Path path = null;
            String cacheKey;
            boolean containPoint = key.contains(".");
            if (containPoint) {
                int lastIndex = key.lastIndexOf(".");
                String orderAttr = key.substring(lastIndex + 1);
                cacheKey = key.substring(0, lastIndex);
                if (cacheMap.containsKey(cacheKey)) {
                    path = cacheMap.get(cacheKey).get(orderAttr);
                } else {
                    ArrayList<String> attrNames = new ArrayList<>();
                    String temp = cacheKey;
                    while (temp.contains(".")) {
                        temp = temp.substring(0, temp.lastIndexOf("."));
                        attrNames.add(temp);
                    }

                    int index = -1;
                    for (String str : attrNames) {
                        index++;
                        if (cacheMap.containsKey(str)) {
                            path = cacheMap.get(str);
                            break;
                        }
                    }
                    if (path != null) {
                        if (index == 0) {
                            // 获取到 company.dept 对应的 joinPath
                            // root.join("company").join("dept").join("emp")
                            path = ((Join) path).join(cacheKey.substring(cacheKey.lastIndexOf(".") + 1));
                        } else {
                            int i = index - 1;
                            do {
                                if (i == 0) {
                                    // 生成 company.dept 对应的 joinPath
                                    // root.join("company").join("dept").join("emp")
                                    path = ((Join) path).join(cacheKey.substring(cacheKey.lastIndexOf(".") + 1));
                                } else {
                                    // 生成 company 对应的 joinPath
                                    // root.join("company").join("dept")
                                    String attr = attrNames.get(i);
                                    String subAttr = attr.substring(attr.lastIndexOf("." + 1));
                                    path = ((Join) path).join(subAttr);
                                }
                                i--;
                            } while (i > 0);
                        }
                    } else {
                        // 找不到 joinPath
                        //  company.dept.emp 分解为 company、dept、emp
                        String[] joinAttrs = cacheKey.split("\\.");
                        int len = joinAttrs.length;
                        // root.join("company") -> join("dept") -> join("emp")
                        for (int i = 0; i < len; i++) {
                            if (i == 0) {
                                path = root.join(joinAttrs[i]);
                            } else {
                                path = ((Join) path).join(joinAttrs[i]);
                            }
                        }
                    }
                    path = path.get(orderAttr);
                }
            } else {
                path = root.get(key);
            }
            orders.add(new OrderImpl(path, entry.getValue()));
        });*/
        return orders;
    }


    /**
     * e.q. company.dept.emp
     * <p>
     * 从缓存中取path
     */
    private Path getSuitablePath(Root<T> root, JoinTerm joinTerm) {
        String attrName = joinTerm.attrName;
        if (cacheMap.containsKey(attrName)) {
            return cacheMap.get(attrName);
        }

        Path path = null;
        final Operator operator = joinTerm.operator;
        JoinType joinType;
        if (operator.equals(Operator.LEFT_JOIN)) {
            joinType = JoinType.LEFT;
        } else if (operator.equals(Operator.RIGHT_JOIN)) {
            joinType = JoinType.RIGHT;
        } else {
            joinType = JoinType.INNER;
        }

        // eq join emp 直接返回
        if (!attrName.contains(".")) {
            path = root.join(attrName, joinType);
            cacheMap.put(attrName, path);
        } else {
            // 将 company.dept.emp 分解为  company.dept、company 两段存放
            // 存放 company
            ArrayList<String> attrNames = new ArrayList<>();
            String temp = attrName;
            while (temp.contains(".")) {
                temp = temp.substring(0, temp.lastIndexOf("."));
                attrNames.add(temp);
            }

            // 获取 company.dept 或 company 对应的 joinPath
            int index = -1;
            for (String str : attrNames) {
                index++;
                if (cacheMap.containsKey(str)) {
                    path = cacheMap.get(str);
                    break;
                }
            }

            // path 不等与null, root.get(attrName).join(..)的缓存对象
            if (path != null) {
                if (index == 0) {
                    // 获取到 company.dept 对应的 joinPath
                    // root.join("company").join("dept").join("emp")
                    path = ((Join) path).join(attrName.substring(attrName.lastIndexOf(".") + 1), joinType);
                    cacheMap.put(attrName, path);
                } else {
                    int i = index - 1;
                    do {
                        if (i == 0) {
                            // 生成 company.dept 对应的 joinPath
                            // root.join("company").join("dept").join("emp")
                            path = ((Join) path).join(attrName.substring(attrName.lastIndexOf(".") + 1), joinType);
                            cacheMap.put(attrName, path);
                        } else {
                            // 生成 company 对应的 joinPath
                            // root.join("company").join("dept")
                            String attr = attrNames.get(i);
                            String subAttr = attr.substring(attr.lastIndexOf("." + 1));
                            path = ((Join) path).join(subAttr);
                            cacheMap.put(attr, path);
                        }
                        i--;
                    } while (i > 0);
                }
            } else {
                // 找不到 joinPath
                //  company.dept.emp 分解为 company、dept、emp
                String[] joinAttrs = attrName.split("\\.");
                int len = joinAttrs.length;
                String attr = "";
                // root.join("company") -> join("dept") -> join("emp")
                for (int i = 0; i < len; i++) {
                    if (i == 0) {
                        attr = joinAttrs[i];
                        path = root.join(attr);
                    } else if (i == len - 1) {
                        path = ((Join) path).join(joinAttrs[i], joinType);
                        attr += "." + joinAttrs[i];
                    } else {
                        path = ((Join) path).join(joinAttrs[i]);
                        attr += "." + joinAttrs[i];
                    }
                    cacheMap.put(attr, path);
                }
            }
        }
        return path;
    }

    @SuppressWarnings("unchecked")
    private Predicate just(Path root, CriteriaBuilder cb, Term term) {
        Predicate predicate = null;
        final Operator operator = term.operator;
        final String attrName = term.attrName;
        final Object value = term.value;
        if (operator != null) {
            switch (operator) {
                case EQ:
                    predicate = cb.equal(root.get(attrName), value);
                    break;
                case NE:
                    predicate = cb.notEqual(root.get(attrName), value);
                    break;
                case LT:
                    predicate = cb.lt(root.get(attrName), Double.parseDouble(String.valueOf(value)));
                    break;
                case GT:
                    predicate = cb.gt(root.get(attrName), Double.parseDouble(String.valueOf(value)));
                    break;
                case GTE:
                    predicate = cb.ge(root.get(attrName), Double.parseDouble(String.valueOf(value)));
                    break;
                case LTE:
                    predicate = cb.le(root.get(attrName), Double.parseDouble(String.valueOf(value)));
                    break;
                case LIKE:
                    predicate = cb.like(root.get(attrName), String.valueOf(value));
                    break;
                case IN:
                    predicate = root.get(attrName).in(value);
                    break;
                case NULL:
                    predicate = root.get(attrName).isNull();
                    break;
                case NOT_NULL:
                    predicate = root.get(attrName).isNotNull();
                    break;
                case EMPTY:
                    predicate = cb.isEmpty(root.get(attrName));
                    break;
                case NOT_EMPTY:
                    predicate = cb.isNotEmpty(root.get(attrName));
                    break;
                case BETWEEN:
                    predicate = cb.between(root.get(attrName)
                        , Double.parseDouble(String.valueOf(term.betweenValue[0]))
                        , Double.parseDouble(String.valueOf(term.betweenValue[1])));
                    break;
                case TRUE:
                    predicate = cb.isTrue(root.get(attrName));
                    break;
                case FALSE:
                    predicate = cb.isFalse(root.get(attrName));
                    break;
            }
        }
        return predicate;
    }

    private Predicate justJoin(Path path, CriteriaBuilder cb, Term term) {
        Predicate predicate = null;
        JoinTerm joinTerm = (JoinTerm) term;
        final LinkedList<Term> simpleTerms = joinTerm.getSimpleTerms();
        for (Term t : simpleTerms) {
            if (predicate == null) {
                predicate = t.and ? cb.and(just(path, cb, t)) : cb.or(just(path, cb, t));
            } else {
                predicate = t.and ? cb.and(predicate, just(path, cb, t)) : cb.or(predicate, just(path, cb, t));
            }
        }
        return predicate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<T> {
        private boolean and;
        private ArrayList<Term> andTerms;
        private ArrayList<Term> orTerms;
        private LinkedList<Term> joinTerms;
        private boolean join;
        private LinkedHashMap<String, Boolean> sortMap;
        private boolean distinct;

        public Builder and() {
            this.and = true;
            return this;
        }

        public Builder or() {
            this.and = false;
            return this;
        }

        public Builder eq(String attrName, Object value) {
            addTerms(Term.eq(attrName, value, and));
            return this;
        }

        public Builder ne(String attrName, Object value) {
            addTerms(Term.ne(attrName, value, and));
            return this;
        }

        public Builder like(String attrName, Object value) {
            addTerms(Term.like(attrName, value, and, null));
            return this;
        }

        public Builder rightLike(String attrName, Object value) {
            addTerms(Term.like(attrName, value, and, true));
            return this;
        }

        public Builder leftLike(String attrName, Object value) {
            addTerms(Term.like(attrName, value, and, false));
            return this;
        }

        public Builder gt(String attrName, Object value) {
            addTerms(Term.gt(attrName, value, and));
            return this;
        }

        public Builder gte(String attrName, Object value) {
            addTerms(Term.gte(attrName, value, and));
            return this;
        }

        public Builder lt(String attrName, Object value) {
            addTerms(Term.lt(attrName, value, and));
            return this;
        }

        public Builder lte(String attrName, Object value) {
            addTerms(Term.lte(attrName, value, and));
            return this;
        }

        public Builder between(String attrName, Object a, Object b) {
            addTerms(Term.between(attrName, a, b, and));
            return this;
        }

        public Builder in(String attrName, Collection value) {
            addTerms(Term.in(attrName, value, and));
            return this;
        }

        public Builder isNull(String attrName) {
            addTerms(Term.isNull(attrName, and));
            return this;
        }

        public Builder notNull(String attrName) {
            addTerms(Term.notNull(attrName, and));
            return this;
        }

        public Builder empty(String attrName) {
            addTerms(Term.empty(attrName, and));
            return this;
        }

        public Builder notEmpty(String attrName) {
            addTerms(Term.notEmpty(attrName, and));
            return this;
        }

        public Builder isTrue(String attrName) {
            addTerms(Term.isTrue(attrName, and));
            return this;
        }

        public Builder isFalse(String attrName) {
            addTerms(Term.isFalse(attrName, and));
            return this;
        }

        @Deprecated
        public Builder asc(String... attrNames) {
            for (String attrName : attrNames) {
                addSort(attrName, true);
            }
            return this;
        }

        @Deprecated
        public Builder desc(String... attrNames) {
            for (String attrName : attrNames) {
                addSort(attrName, false);
            }
            return this;
        }

        public Builder distinct() {
            this.distinct = true;
            return this;
        }

        public Builder leftJoin(String attrName) {
            this.join = true;
            addJoinTerms(new JoinTerm(attrName, Operator.LEFT_JOIN, and));
            return this;
        }

        public Builder rightJoin(String attrName) {
            this.join = true;
            addJoinTerms(new JoinTerm(attrName, Operator.RIGHT_JOIN, and));
            return this;
        }

        public Builder innerJoin(String attrName) {
            this.join = true;
            addJoinTerms(new JoinTerm(attrName, Operator.INNER_JOIN, and));
            return this;
        }

        public Builder joinEnd() {
            this.join = false;
            return this;
        }

        public EasySpecification<T> build() {
            return new EasySpecification(this);
        }

        private void addTerms(Term term) {
            if (this.join) {
                JoinTerm joinTerm = (JoinTerm) joinTerms.getLast();
                joinTerm.addSimpleTerm(term);
            } else {
                if (this.and) {
                    if (this.andTerms == null) {
                        this.andTerms = new ArrayList<>();
                    }
                    this.andTerms.add(term);
                } else {
                    if (this.orTerms == null) {
                        this.orTerms = new ArrayList<>();
                    }
                    this.orTerms.add(term);
                }
            }
        }

        private void addJoinTerms(Term term) {
            if (this.joinTerms == null) {
                this.joinTerms = new LinkedList<>();
            }
            this.joinTerms.add(term);
        }

        private void addSort(String attrName, boolean asc) {
            if (sortMap == null) {
                sortMap = new LinkedHashMap<>();
            }
            sortMap.put(attrName, asc);
        }
    }
}

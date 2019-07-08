package com.hh.web.serviceapi.base;


import com.hh.web.serviceapi.base.spec.EasySpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Created by jackson on 2018/7/9.
 */
@Slf4j
@SuppressWarnings("unchecked")
public abstract class BaseService<Repo extends BaseRepository> {

    @Autowired
    protected Repo repository;

    @SuppressWarnings("unused")
    @PersistenceContext
    protected EntityManager entityManager;

    protected EasySpecification.Builder builder() {
        return new EasySpecification.Builder();
    }

    protected boolean checkIsExisted(String fieldName, Object value) {
        return repository.count(builder().eq(fieldName, value).build()) > 0;
    }

    protected <S extends BaseEntity> S saveOrUpdate(S s) {
        return (S) repository.save(s);
    }

    protected void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public <S extends BaseEntity> S findById(Integer id) {
        return (S) repository.findById(id).orElse(null);
    }

    public <S extends BaseEntity> S find(String fieldName, Object value) {
        Optional one = repository.findOne(builder().eq(fieldName, value).build());
        return one.isPresent() ? (S) one.get() : null;
    }

    public long count(String fieldName, Object value) {
        return repository.count(builder().eq(fieldName, value).build());
    }

    protected Page findAll(Specification spec, int index, int size) {
        return repository.findAll(spec, PageRequest.of(index, size));
    }

    protected Pages toPages(Page page) {
        Pages pages = new Pages();
        if (page != null) {
            pages.setIndex(page.getNumber());
            pages.setSize(page.getSize());
            pages.setContent(page.getContent());
            pages.setTotal(page.getTotalElements());
            pages.setTotalPages(page.getTotalPages());
        }
        return pages;
    }


    @Transactional(readOnly = true)
    public Pages fullSearch(FullSearchQuery fsq) {
        Pages pages = new Pages();
        int index = fsq.getIndex() < 0 ? 0 : fsq.getIndex();
        int size = fsq.getSize() <= 0 ? 10 : fsq.getSize();
        pages.setIndex(index);
        pages.setSize(size);
        FullTextEntityManager fullTextEntityManager = null;
        try {
            fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            try {
                fullTextEntityManager.createIndexer().startAndWait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            QueryBuilder queryBuilder = fullTextEntityManager
                    .getSearchFactory()
                    .buildQueryBuilder()
                    .forEntity(fsq.getQueryCls())
                    .get();
            Query query = queryBuilder
                    .keyword()
                    .fuzzy()
                    .onFields(fsq.getFields())
                    .matching(fsq.getValue())
                    .createQuery();
            FullTextQuery jpaQuery = fullTextEntityManager
                    .createFullTextQuery(query, fsq.getQueryCls())
                    .setFirstResult(index)
                    .setMaxResults(size);
            List resultList = jpaQuery.getResultList();
            int maxResults = jpaQuery.getResultSize();
            pages.setTotal(maxResults);
            if (maxResults % size == 0) {
                pages.setTotalPages(maxResults / size);
            } else {
                pages.setTotalPages(maxResults / size + 1);
            }
            pages.setContent(resultList);
        } finally {
            if (fullTextEntityManager != null) {
                fullTextEntityManager.close();
            }
        }
        return pages;
    }
}

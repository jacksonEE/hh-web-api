package com.hh.web.serviceapi.base;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @CreatedDate
    @Column(nullable = false, columnDefinition = "datetime(0) default now()")
    protected LocalDateTime createTime;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "datetime(0) default now()")
    protected LocalDateTime updateTime;
}

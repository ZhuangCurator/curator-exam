package com.curator.common.support;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果
 *
 * @author Jun
 * @date 2021/4/15
 */
@Data
@Builder
public class PageResult<T> implements Serializable {

    /**
     * 默认页码
     */
    private static final long DEFAULT_PAGE_NO = 1;

    /**
     * 默认页面大小
     */
    private static final long DEFAULT_PAGE_SIZE = 20;

    /**
     * 当前页码
     */
    private long current;

    /**
     * 当前页面大小
     */
    private long size;

    /**
     * 总的记录数
     */
    private long total;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 返回的查询结果集
     */
    private List<T> records;

}


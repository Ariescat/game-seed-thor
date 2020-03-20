package com.windforce.common.ramcache.service;

import com.windforce.common.ramcache.IEntity;

import java.io.Serializable;

/**
 * 区域增强服务接口
 *
 * @param <PK>
 * @param <T>
 * @author frank
 */
public interface RegionEnhanceService<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> extends EnhanceService<PK, T> {

    /**
     * 修改索引值的方法
     *
     * @param name   索引属性名
     * @param entity 修改的实体
     * @param prev   之前的值
     */
    void changeIndexValue(String name, T entity, Object prev);

}

package com.windforce.common.ramcache.sample.entity.init;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.*;
import com.windforce.common.ramcache.enhance.Enhance;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 测试实体一
 *
 * @author frank
 */
@Entity
@Cached(size = "default", persister = @Persister("test"), type = CacheType.MANUAL)
@InitialConfig(type = InitialType.ALL)
public class EntityOne implements IEntity<Integer> {

    /**
     * 标识
     */
    @Id
    private Integer id;
    /**
     * 用户名
     */
    private String name;

    // Getter and Setter ...

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Enhance
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean serialize() {
        // TODO Auto-generated method stub
        return false;
    }

}

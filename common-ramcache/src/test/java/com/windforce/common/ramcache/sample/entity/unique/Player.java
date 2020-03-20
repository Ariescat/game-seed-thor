package com.windforce.common.ramcache.sample.entity.unique;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.ChkUnique;
import com.windforce.common.ramcache.anno.Unique;
import com.windforce.common.ramcache.enhance.Enhance;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * 用户对象实体
 *
 * @author frank
 */
@Entity
@Cached(size = "default")
@NamedQueries({@NamedQuery(name = "Player.name", query = "from Player where name = ?")})
public class Player implements IEntity<Integer> {

    /**
     * 用户标识
     */
    @Id
    private Integer id;
    /**
     * 用户名
     */
    @Unique(query = "Player.name")
    private String name;

    public Integer getId() {
        return id;
    }

    // Getter and Setter ...

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Enhance
    public void setName(@ChkUnique("name") String name) {
        this.name = name;
    }

    @Override
    public boolean serialize() {
        // TODO Auto-generated method stub
        return false;
    }

}

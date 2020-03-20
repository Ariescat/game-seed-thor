package com.windforce.common.ramcache.service;

import com.windforce.common.ramcache.IEntity;

import java.io.Serializable;

public interface EntityBuilder<PK extends Comparable<PK> & Serializable, T extends IEntity<PK>> {

    T newInstance(PK id);

}

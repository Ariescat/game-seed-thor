package com.windforce.common.ramcache.enhance;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.CacheUnit;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.ChangeIndex;
import com.windforce.common.ramcache.anno.Index;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@Cached(size = "default", unit = CacheUnit.REGION)
@NamedQueries({@NamedQuery(name = "Item.owner", query = "from Item i where i.owner = ?")})
public class Item implements IEntity<Integer> {

	@Id
	private Integer id;
	@Index(query = "Item.owner")
	private int owner;

	public Integer getId() {
		return id;
	}

	// Getter and Setter ...

	protected void setId(Integer id) {
		this.id = id;
	}

	public int getOwner() {
		return owner;
	}

	@Enhance
	public void setOwner(@ChangeIndex("owner") int owner) {
		this.owner = owner;
	}

	@Override
	public boolean serialize() {
		// TODO Auto-generated method stub
		return false;
	}

}

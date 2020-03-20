package com.windforce.common.utility.chain.impl;

import com.windforce.common.utility.chain.NodeProcessor;
import com.windforce.common.utility.chain.Notice;
import com.windforce.common.utility.chain.Way;

/**
 * 抽象节点处理器
 *
 * @author frank
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractNode implements NodeProcessor {

    private final String name;
    private final int index;
    private final Way way;

    public AbstractNode(String name, int index, Way way) {
        this.name = name;
        this.index = index;
        this.way = way;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public Way getWay() {
        return way;
    }

    @Override
    public int compareTo(NodeProcessor o) {
        if (this.getIndex() > o.getIndex()) {
            return 1;
        } else if (this.getIndex() < o.getIndex()) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean in(Notice notice) {
        return true;
    }

    @Override
    public boolean out(Notice notice) {
        return true;
    }

}

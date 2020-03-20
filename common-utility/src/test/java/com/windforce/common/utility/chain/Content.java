package com.windforce.common.utility.chain;

import java.util.HashMap;
import java.util.Map;

public class Content {

    private Map<String, Object> values = new HashMap<String, Object>();

    public void setValue(String name, Object value) {
        values.put(name, value);
    }

    public Object getValue(String name) {
        return values.get(name);
    }

}

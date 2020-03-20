package com.windforce.common.resource.support;

import com.windforce.common.utility.JsonUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;

public class StringToMapConverter implements Converter<String, Map<String, Object>> {

    @Override
    public Map<String, Object> convert(String source) {
        return JsonUtils.string2Map(source);
    }

}

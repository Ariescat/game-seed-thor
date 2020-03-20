package com.windforce.common.ramcache.aop;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TestTarget {

	@AutoLocked
	public void methodObject(@IsLocked Object obj1, @IsLocked Object obj2) {
	}

	@AutoLocked
	public void methodList(@IsLocked(element = true) List<Object> list) {
	}

	@AutoLocked
	public void methodArrayOne(@IsLocked(element = true) Object... objs) {
	}

	@AutoLocked
	public void methodArrayTwo(@IsLocked(element = true) Object[] objs) {
	}

	@SuppressWarnings("rawtypes")
	@AutoLocked
	public void methodMap(@IsLocked(element = true) Map map) {
	}

}

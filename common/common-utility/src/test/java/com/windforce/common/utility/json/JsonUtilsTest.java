package com.windforce.common.utility.json;

import com.windforce.common.utility.JsonUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtilsTest {

	@Test
	public void testMapWithObjectKey() {
		final Map<String, Key> map = new HashMap<String, Key>();
		map.put("key", new Key("value"));
		System.out.println(JsonUtils.map2String(map));

	}

	@Test
	public void testMapWithListKey() {
		final Map<List<String>, String> map = new HashMap<List<String>, String>();
		List<String> key = new ArrayList<String>();
		key.add("key");
		map.put(key, "value");
		System.out.println(JsonUtils.map2String(map));
	}

	public static class Key {

		private Object key;

		public Key() {
		}

		public Key(Object key) {
			this.key = key;
		}

		public Object getKey() {
			return key;
		}

		public void setKey(Object key) {
			this.key = key;
		}

	}

}

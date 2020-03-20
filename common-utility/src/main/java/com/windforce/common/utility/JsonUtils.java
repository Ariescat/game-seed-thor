package com.windforce.common.utility;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * JSON 转换相关的工具类 注意,Map的Key只能为简单类型 ,不可采用复杂类型.
 *
 * @author Frank
 */
public final class JsonUtils {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
	private static TypeReference<Map<String, Object>> mapType = new TypeReference<Map<String, Object>>() {
	};

	private JsonUtils() {
		throw new IllegalAccessError("该类不允许实例化");
	}

	/**
	 * 将对象转换为 JSON 的字符串格式
	 *
	 * @param object
	 * @return
	 */
	public static String object2String(Object object) {
		return JSON.toJSONString(object, false);
	}

	/**
	 * 将 map 转换为 JSON 的字符串格式
	 *
	 * @param map
	 * @return
	 */
	public static String map2String(Map<?, ?> map) {
		return JSON.toJSONString(map, false);
	}

	/**
	 * 将 JSON 格式的字符串转换为 map
	 *
	 * @param content
	 * @return
	 */
	public static Map<String, Object> string2Map(String content) {
		return JSON.parseObject(content, mapType);
	}

	/**
	 * 将 JSON 格式的字符串转换为list
	 *
	 * @param <T>
	 * @param content 字符串
	 * @param clz     数组类型
	 * @return
	 */
	public static <T> List<T> string2List(String content, Class<T> clz) {
		return JSON.parseArray(content, clz);
	}

	/**
	 * 将 JSON 格式的字符串转换为对象
	 *
	 * @param <T>
	 * @param content 字符串
	 * @param clz     对象类型
	 * @return
	 */
	public static <T> T string2Object(String content, Class<T> clz) {
		return JSON.parseObject(content, clz);
	}

	public static <K, V> Map<K, V> string2Map(String content, Class<K> keyType, Class<V> valueType) {
		TypeReference<Map<K, V>> mapType = new TypeReference<Map<K, V>>() {
		};
		return JSON.parseObject(content, mapType);
	}
}

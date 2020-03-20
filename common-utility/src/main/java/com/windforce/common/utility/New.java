package com.windforce.common.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class contains static methods to construct commonly used generic objects
 * such as ArrayList.
 */
public class New {

	/**
	 * Create a new ArrayList.
	 *
	 * @param <T> the type
	 * @return the object
	 */
	public static <T> ArrayList<T> arrayList() {
		return new ArrayList<T>(4);
	}

	/**
	 * Create a new HashMap.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return the object
	 */
	public static <K, V> HashMap<K, V> hashMap() {
		return new HashMap<K, V>();
	}

	/**
	 * Create a new HashMap.
	 *
	 * @param <K>             the key type
	 * @param <V>             the value type
	 * @param initialCapacity the initial capacity
	 * @return the object
	 */
	public static <K, V> HashMap<K, V> hashMap(int initialCapacity) {
		return new HashMap<K, V>(initialCapacity);
	}

	public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap() {
		return new ConcurrentHashMap<K, V>();
	}

	/**
	 * Create a new HashSet.
	 *
	 * @param <T> the type
	 * @return the object
	 */
	public static <T> HashSet<T> hashSet() {
		return new HashSet<T>();
	}

	/**
	 * Create a new ArrayList.
	 *
	 * @param <T> the type
	 * @param c   the collection
	 * @return the object
	 */
	public static <T> ArrayList<T> arrayList(Collection<T> c) {
		return new ArrayList<T>(c);
	}

	/**
	 * Create a new ArrayList.
	 *
	 * @param <T>             the type
	 * @param initialCapacity the initial capacity
	 * @return the object
	 */
	public static <T> ArrayList<T> arrayList(int initialCapacity) {
		return new ArrayList<T>(initialCapacity);
	}

}

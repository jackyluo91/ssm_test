package com.example.ssm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * 适用于储存树状结构的数据集
 *
 * @author luojiaheng
 * @date 2016年10月19日
 * @time 下午5:52:54
 * @param <K>
 *            key(需注意是否可能重复)
 * @param <V>
 *            value
 */
public class MapObject<K, V> {
	private LinkedHashMap<K, MapObject<K, V>> map;
	private V obj;

	public MapObject() {
		map = new LinkedHashMap<>();
	}

	public MapObject(V obj) {
		this();
		this.obj = obj;
	}

	/**
	 * 当需要在map中插入Value时使用
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {
		MapObject<K, V> lom = new MapObject<>(value);
		map.put(key, lom);
	}

	/**
	 * 递归找到key对应的所有的value及子集中的value
	 * 
	 * @return
	 */
	public ArrayList<V> getValues(K key) {
		ArrayList<V> list = new ArrayList<>();
		if (map.keySet().contains(key)) {
			list.addAll(map.get(key).values());
		} else if (!map.isEmpty()) {
			Iterator<MapObject<K, V>> it = map.values().iterator();
			while (it.hasNext()) {
				list.addAll(it.next().getValues(key));
			}
		}
		return list;
	}

	/**
	 * 递归找到key对应的所有的key及子集中的key
	 * 
	 * @return
	 */
	public LinkedHashSet<K> getKeys(K key) {
		LinkedHashSet<K> set = new LinkedHashSet<>();
		if (map.keySet().contains(key)) {
			set.addAll(map.get(key).keySet());
		} else if (!map.isEmpty()) {
			Iterator<MapObject<K, V>> it = map.values().iterator();
			while (it.hasNext()) {
				set.addAll(it.next().getKeys(key));
			}
		}
		return set;
	}

	/**
	 * 递归找到所有的value
	 * 
	 * @return
	 */
	public ArrayList<V> values() {
		ArrayList<V> list = new ArrayList<>();
		if (map.isEmpty()) {
			list.add(obj);
		} else {
			Iterator<MapObject<K, V>> it = map.values().iterator();
			while (it.hasNext()) {
				list.addAll(it.next().values());
			}
		}
		return list;
	}

	/**
	 * 递归找到所有的key
	 * 
	 * @return
	 */
	public LinkedHashSet<K> keySet() {
		LinkedHashSet<K> set = new LinkedHashSet<>();
		if (!map.isEmpty()) {
			set.addAll(map.keySet());
			Iterator<MapObject<K, V>> it = map.values().iterator();
			while (it.hasNext()) {
				set.addAll(it.next().keySet());
			}
		}
		return set;
	}

	public HashMap<K, MapObject<K, V>> getMap() {
		return map;
	}

	public V getObject() {
		return obj;
	}

}

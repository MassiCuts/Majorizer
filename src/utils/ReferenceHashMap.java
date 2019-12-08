package utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReferenceHashMap<T, E> implements Map<T, E> {
	private HashMap<T, E> map = new HashMap<>();

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public E get(Object key) {
		return map.get(key);
	}

	@Override
	public E put(T key, E value) {
		return map.put(key, value);
	}

	@Override
	public E remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends T, ? extends E> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<T> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<E> values() {
		return map.values();
	}

	@Override
	public Set<Entry<T, E>> entrySet() {
		return map.entrySet();
	}
}
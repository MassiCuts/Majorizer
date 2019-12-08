package utils;

import java.util.ArrayList;

public class ArrayUtils {
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> create(T[] elements) {
		ArrayList<T> array = new ArrayList<>();
		for(Object element : elements)
			array.add((T) element);
		return array;
	}
}

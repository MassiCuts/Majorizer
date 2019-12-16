package utils;

import java.util.ArrayList;
import java.util.Iterator;

public class ArrayIterable<T> implements Iterable<T> {
	private ArrayList<T> array;
	
	public ArrayIterable(ArrayList<T> array) {
		this.array = array;
	}
	
	@Override
	public Iterator iterator() {
		return array.iterator();
	}
	
}

package utils;

public class Single <T> {
	public T single;
	
	public Single(T single) {
		this.single = single;
	}
	
	public Single() {
		this(null);
	}
}

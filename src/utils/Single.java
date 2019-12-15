package utils;

public class Single <T> {
	public volatile T single;
	
	public Single(T single) {
		this.single = single;
	}
	
	public Single() {
		this(null);
	}
}

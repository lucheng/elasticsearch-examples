package org.cheng.elasticsearch;

public class JavaTest {
	private volatile String a;

	private String b;

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public int inc() {
		int x;
		int j;
		try {
			x = 1;
			j = 2;
			return x+j;
		} catch (Exception e) {
			x = 2;
			return x;
		} finally {
			x = 3;
		}
	}

	public synchronized int ind() {
		int x;
		try {
			x = 1;
			return x;
		} catch (Exception e) {
			x = 2;
			return x;
		} finally {
			x = 3;
		}
	}

	public int ine() {
		synchronized (this.getClass()) {
			int x;
			int j;
			try {
				x = 1;
				j = 2;
				return x+j;
			} catch (Exception e) {
				x = 2;
				return x;
			} finally {
				x = 3;
			}
		}
	}

	public static void main(String[] args) {
		JavaTest j = new JavaTest();
		System.out.println(j.inc());
	}
}

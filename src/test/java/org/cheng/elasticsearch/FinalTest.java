package org.cheng.elasticsearch;

import java.util.ArrayList;
import java.util.List;

public class FinalTest {

	public static final Integer list;
	
	static {
		list = 1;
	}

	public static void main(String[] args) {
		
		new Thread(() -> {
			FinalTest ft = new FinalTest();
			while (ft.list == null) {
			}
			System.out.println("list 大小改变");
		}).start();

		new Thread(() -> {
			FinalTest ft = new FinalTest();
			System.out.println(ft.list);
		}).start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		FinalTest ft = new FinalTest();
		System.out.println(ft.list);
	}
}
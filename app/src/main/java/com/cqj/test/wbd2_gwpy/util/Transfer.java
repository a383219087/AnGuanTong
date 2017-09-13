package com.cqj.test.wbd2_gwpy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Stack;

/* * 本程序 */
public class Transfer {
	public Stack<Integer> transfer(int n) {
		Stack<Integer> st = new Stack<Integer>();
		int division = 0; // 余数
		while (n >= 10) {
			division = n % 10;
			st.push(division);
			n = n / 10;
		}
		st.push(n);
		// 将最高位压栈
		return st;
	}

	public static void main(String[] args) {
//		BufferedReader reader = new BufferedReader(new InputStreamReader(
//				System.in));
//		String in = "";
//		try {
//			in = reader.readLine();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		int n = 0;
//		try {
//			n = Integer.parseInt(in);
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		}
		Transfer tf = new Transfer();
		Stack<Integer> s = tf.transfer(3); /*
											 * while(!s.empty()){
											 * System.out.print(s.pop()); //测试语句
											 * }
											 */
		HashMap<Integer, String> hp1 = new HashMap<Integer, String>(); // 第一个映射表
		hp1.put(0, "零");
		// 根据所在位的数值与中文对应
		hp1.put(1, "一");
		hp1.put(2, "二");
		hp1.put(3, "三");
		hp1.put(4, "四");
		hp1.put(5, "五");
		hp1.put(6, "六");
		hp1.put(7, "七");
		hp1.put(8, "八");
		hp1.put(9, "九");
		HashMap<Integer, String> hp2 = new HashMap<Integer, String>(); // 第二个映射表
		hp2.put(2, "十"); // 根据所在位数，与中文对应
		hp2.put(3, "百");
		hp2.put(4, "千");
		hp2.put(5, "万");
		hp2.put(6, "十万");
		hp2.put(7, "百万");
		hp2.put(8, "千万");
		hp2.put(9, "亿"); // System.out.println(s.size());
		String out = "";
		while (!s.isEmpty()) {
			int temp = s.pop();
			if (s.size() == 0) {
				if (temp != 0) {
					out = out + hp1.get(temp);
				}
			} else {
				if (temp == 0) {
					out = out + hp1.get(temp);
				} else {
					out = out + hp1.get(temp) + hp2.get(s.size() + 1);
				}
			}
		}
		System.out.println(out);
	}
}

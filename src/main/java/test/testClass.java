package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class testClass {
	public static Method[] method;

	public static void main(String[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		SampleClass sc = new SampleClass();
		method = sc.getClass().getMethods();
		int total = method.length;

		for (int i = 0; i < total; i++) {
			System.out.println(method[i]);
			if (method[i].getParameterCount() == 3) {
				method[i].invoke(sc, "a", "b", "c");
			}
		}
	}

}

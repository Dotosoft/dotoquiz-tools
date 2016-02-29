package com.dotosoft.dotoquiz.tools.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SingletonTools {
	private static final Map<String, Object> INSTANCES = new HashMap<>();

	private static class SingletonHolder<T> {
		private static <T> T getInstance(Class<T> clazz)
				throws InstantiationException, IllegalAccessException,
				NoSuchMethodException, SecurityException,
				IllegalArgumentException, InvocationTargetException {
			Constructor<T> constructor = (Constructor<T>) clazz.getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			return constructor.newInstance(null);
		}
	}

	public static <T> T getInstance(Class<T> clazz) throws InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {
		if (INSTANCES.containsKey(clazz)) {
			return (T) INSTANCES.get(clazz);
		} else {
			T instance = SingletonHolder.getInstance(clazz);
			INSTANCES.put(clazz.getName(), instance);
			return instance;
		}
	}

	public static <T extends Object> void putInstance( Class<T> clazz, T instance ) {
		if (!INSTANCES.containsKey(clazz)) {
			INSTANCES.put(clazz.getName(), instance);
		}
	}
}
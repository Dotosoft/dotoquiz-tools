//package com.dotosoft.dotoquiz.tools.util;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.util.HashMap;
//import java.util.Map;
//
//public abstract class AbstractXMLParser<T> {
//	private static final Map<Class<? extends AbstractXMLParser>, AbstractXMLParser> INSTANCES = new HashMap<>();
//
//	public AbstractXMLParser() {
//
//	}
//
//	private static class SingletonHolder<T> {
//		private static <T> T getInstance(Class<T> clazz)
//				throws InstantiationException, IllegalAccessException,
//				NoSuchMethodException, SecurityException,
//				IllegalArgumentException, InvocationTargetException {
//			Constructor<T> constructor = (Constructor<T>) clazz
//					.getDeclaredConstructors()[0];
//			constructor.setAccessible(true);
//			return constructor.newInstance(null);
//		}
//	}
//
//	protected static <T extends AbstractXMLParser<T>> T getInstance(
//			Class<T> clazz) throws InstantiationException,
//			IllegalAccessException, NoSuchMethodException, SecurityException,
//			IllegalArgumentException, InvocationTargetException {
//		if (INSTANCES.containsKey(clazz)) {
//			return (T) INSTANCES.get(clazz);
//		} else {
//			T instance = SingletonHolder.getInstance(clazz);
//			INSTANCES.put(clazz, instance);
//			return instance;
//		}
//	}
//
//	protected static <T extends AbstractXMLParser<T>> void putInstance(
//			Class<T> clazz, T instance) {
//		if (!INSTANCES.containsKey(clazz)) {
//			INSTANCES.put(clazz, instance);
//		}
//	}
//}
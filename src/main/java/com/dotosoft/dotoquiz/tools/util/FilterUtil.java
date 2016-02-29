package com.dotosoft.dotoquiz.tools.util;

import java.util.ArrayList;
import java.util.Collection;

import com.dotosoft.dotoquiz.tools.metadata.Predicate;

public class FilterUtil {
	public static <T> Collection<T> filter(Collection<T> col, Predicate<T> predicate) {
		Collection<T> result = new ArrayList<T>();
		for (T element : col) {
			if (predicate.apply(element)) {
				result.add(element);
			}
		}
		return result;
	}
}
package com.dotosoft.dotoquiz.tools.metadata;

public interface Predicate<T> {
	boolean apply(T type);
}
package com.dotosoft.dotoquiz.command.generic;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructors;

public class SetPropertyCommand implements Command {

	/** Primitive type name -> class map. */
	private static final Map PRIMITIVE_NAME_TYPE_MAP = new HashMap();

	/** Setup the primitives map. */
	static {
		PRIMITIVE_NAME_TYPE_MAP.put("boolean", Boolean.class);
		PRIMITIVE_NAME_TYPE_MAP.put("byte", Byte.class);
		PRIMITIVE_NAME_TYPE_MAP.put("char", Character.class);
		PRIMITIVE_NAME_TYPE_MAP.put("short", Short.class);
		PRIMITIVE_NAME_TYPE_MAP.put("int", Integer.class);
		PRIMITIVE_NAME_TYPE_MAP.put("long", Long.class);
		PRIMITIVE_NAME_TYPE_MAP.put("float", Float.class);
		PRIMITIVE_NAME_TYPE_MAP.put("double", Double.class);
	}
	
	private String type;
	private String value;
	private String toKey;
	
	public void setType(String type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if(PRIMITIVE_NAME_TYPE_MAP.containsKey(type.toLowerCase())) {
			Class clazz = (Class) PRIMITIVE_NAME_TYPE_MAP.get(type.toLowerCase());
			Object returnValue = clazz.getConstructor(String.class).newInstance(value);
			context.put(toKey, returnValue);
		} else {
			context.put(toKey, value);
		}
		
		return false;
	}

}

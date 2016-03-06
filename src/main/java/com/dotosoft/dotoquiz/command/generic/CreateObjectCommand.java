package com.dotosoft.dotoquiz.command.generic;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

import com.dotosoft.dotoquiz.tools.util.BeanUtils;
import com.dotosoft.dotoquiz.tools.util.SingletonFactory;
import com.dotosoft.dotoquiz.utils.StringUtils;

public class CreateObjectCommand implements Filter {

	private String objectClass;
	private String argumentsKey;
	private String toKey;

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}
	
	public void setArgumentsKey(String argumentsKey) {
		this.argumentsKey = argumentsKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		Class objectClazz = Class.forName(objectClass);

		Object webClient;
		if (StringUtils.hasValue(argumentsKey)) {
			webClient = SingletonFactory.getInstance(objectClazz, getObjects(context));
		} else {
			webClient = SingletonFactory.getInstance(objectClazz);
		}

		context.put(toKey, webClient);
		
		return false;
	}
	
	public boolean postprocess(Context context, Exception exception) {
		if (exception == null) return false;
		exception.printStackTrace();
		System.err.println("Exception " + exception.getMessage() + " occurred.");
		return true;
	}
	
	/**
     * Return a <code>Class[]</code> describing the expected signature of the method.
     * @return The method signature.
     */
    protected Object[] getObjects(Context context) {
    	
    	if(StringUtils.hasValue(argumentsKey)) {
	    	String[] keys = argumentsKey.split(",");
	    	Object[] obj = new Object[keys.length];
	    	for(int i = 0; i<keys.length; i++) {
	    		String key = keys[i];
	    		Object param = BeanUtils.getProperty(context, key);
	    		obj[i] = param;
	    	}
	    	return obj;
    	}
    	
        return null;
    }

}

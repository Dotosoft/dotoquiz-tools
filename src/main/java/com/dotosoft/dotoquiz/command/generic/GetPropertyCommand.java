package com.dotosoft.dotoquiz.command.generic;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.dotosoft.dotoquiz.tools.util.BeanUtils;

public class GetPropertyCommand implements Command {


    // -------------------------------------------------------------- Properties
    private String fromKey = null;

    /**
     * <p>Return the context attribute key for the source attribute.</p>
     * @return The source attribute key.
     */
    public String getFromKey() {
    	return (this.fromKey);
    }


    /**
     * <p>Set the context attribute key for the source attribute.</p>
     *
     * @param fromKey The new key
     */
    public void setFromKey(String fromKey) {
    	this.fromKey = fromKey;
    }

    private String toKey = null;
    
    /**
     * <p>Return the context attribute key for the destination attribute.</p>
     * @return The destination attribute key.
     */
    public String getToKey() {
    	return (this.toKey);
    }

    /**
     * <p>Set the context attribute key for the destination attribute.</p>
     *
     * @param toKey The new key
     */
    public void setToKey(String toKey) {
    	this.toKey = toKey;
    }

    // ---------------------------------------------------------- Filter Methods
    /**
     * <p>Copy a specified literal value, or a context attribute stored under
     * the <code>fromKey</code> (if any), to the <code>toKey</code>.</p>
     *
     * @param context {@link Context} in which we are operating
     *
     * @return <code>false</code> so that processing will continue
     * @throws Exception in the if an error occurs during execution.
     */
    public boolean execute(Context context) throws Exception {

        Object value = BeanUtils.getProperty(context, fromKey);

        if (value != null) {
            context.put(toKey, value);
        } else {
            context.remove(toKey);
        }

        return false;

    }


}

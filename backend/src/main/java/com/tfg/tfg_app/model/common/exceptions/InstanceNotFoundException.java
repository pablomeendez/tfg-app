package com.tfg.tfg_app.model.common.exceptions;

@SuppressWarnings("serial")
public class InstanceNotFoundException extends InstanceException {
    
    /**
     * Instantiates a new instance not found exception.
     *
     * @param name the name
     * @param key the key
     */
    public InstanceNotFoundException(String name, Object key) {
    	super(name, key); 	
    }

}
package com.rs.plugin.wrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ObjectSignature {
	
	/**
	 * The Object ID we're interacting with
	 * @return
	 */
	int[] objectId();
	
	/**
	 * The Object Name we're interacting with
	 * @return
	 */
	String[] name();
	
}

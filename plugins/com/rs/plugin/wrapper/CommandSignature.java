package com.rs.plugin.wrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rs.game.player.Rights;

/**
 *
 * @author Dennis
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandSignature {
	
	/**
	 * The aliases of this command.
	 * @return the value identified as a string.
	 */
	String[] alias();
	
	/**
	 * The rights that can use this command.
	 * @return the rights enumerator as an array.
	 */
	Rights[] rights();
	
	/**
	 * The syntax of how an user can use this command.
	 * @return the value identified as a string.
	 */
	String syntax();
	
}

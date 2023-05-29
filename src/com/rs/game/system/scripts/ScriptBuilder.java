package com.rs.game.system.scripts;

import com.rs.game.system.scripts.exceptions.ScriptException;


/**
 * Used for building scripts.
 * @author Emperor
 *
 */
public abstract class ScriptBuilder {

	/**
	 * The builder arguments.
	 */
	protected Object[] arguments;
	
	/**
	 * Constructs a new {@code ScriptBuilder} {@code Object}.
	 */
	public ScriptBuilder(Object[] arguments) {
		this.arguments = arguments;
	}
	
	/**
	 * Parses the builder arguments.
	 * @param args The arguments to parse.
	 * @return The builder arguments.
	 */
	public abstract Object[] parseArguments(String args);
	
	/**
	 * Initializes the script.
	 * @param context The script context.
	 */
	public abstract void configureScript(ScriptContext context);
	
	/**
	 * Creates a new script builder.
	 * @param args The arguments.
	 * @return The builder.
	 */
	public abstract ScriptBuilder create(Object...args);

	/**
	 * Handles a local method.
	 * @param name The method name.
	 * @param context The method script.
	 * @param current The current script.
	 */
	public abstract void handleLocalMethod(String name, ScriptContext context, ScriptContext current) throws ScriptException;
	
	/**
	 * Gets the builder arguments.
	 * @return The arguments.
	 */
	public Object[] getArguments() {
		return arguments;
	}
}
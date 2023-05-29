package com.rs.game.system.scripts;


/**
 * Represents a method call.
 * @author Emperor
 *
 */
public final class MethodCall {

	/**
	 * The method name.
	 */
	private final String methodName;
	
	/**
	 * The arguments.
	 */
	private final String arguments;
	
	/**
	 * The method script.
	 */
	private ScriptContext script;
	
	/**
	 * Constructs a new {@code MethodCall} {@code Object}.
	 * @param methodName The method name.
	 * @param arguments The arguments.
	 */
	public MethodCall(String methodName, String arguments) {
		this(methodName, arguments, null);
	}
	
	/**
	 * Constructs a new {@code MethodCall} {@code Object}.
	 * @param methodName The method name.
	 * @param arguments The arguments.
	 * @param script The script context.
	 */
	public MethodCall(String methodName, String arguments, ScriptContext script) {
		this.methodName = methodName;
		this.arguments = arguments;
		this.script = script;
	}

	/**
	 * Gets the methodName.
	 * @return The methodName.
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Gets the arguments.
	 * @return The arguments.
	 */
	public String getArguments() {
		return arguments;
	}

	/**
	 * Gets the script.
	 * @return The script.
	 */
	public ScriptContext getScript() {
		return script;
	}

	/**
	 * Sets the script.
	 * @param script The script to set.
	 */
	public void setScript(ScriptContext script) {
		this.script = script;
	}
	
}
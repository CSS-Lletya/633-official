package com.rs.game.system.scripts;

/**
 * Represents a parameter call.
 * @author Emperor
 *
 */
public final class ParamCall {

	/**
	 * The parameter name.
	 */
	private final String parameter;
	
	/**
	 * Constructs a new {@code MethodParam} {@code Object}.
	 * @param parameter The parameter name.
	 */
	public ParamCall(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Gets the parameter.
	 * @return The parameter.
	 */
	public String getParameter() {
		return parameter;
	}
}
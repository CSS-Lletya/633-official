package com.rs.game.system.scripts;

/**
 * Script context, this can be a condition or an instruction.
 * @author Emperor
 *
 */
public abstract class ScriptContext {

	/**
	 * The default arguments.
	 */
	private boolean instant = true;
	
	/**
	 * The name of this script.
	 */
	private String name;
	
	/**
	 * The argument line.
	 */
	protected Object[] parameters;
	
	/**
	 * The condition.
	 */
	private ScriptContext condition;

	/**
	 * The instruction.
	 */
	private ScriptContext instruction;

	/**
	 * Constructs a new {@code ScriptContext} {@code Object}.
	 * @param name The script context name.
	 */
	public ScriptContext(String name) {
		this.name = name;
	}

	/**
	 * Parses the parameters and creates a new script context object.
	 * @param params The parameters.
	 * @return The script context.
	 */
	public abstract ScriptContext parse(Object...params);
	
	/**
	 * Executes the script.
	 * @param args The arguments.
	 * @return {@code True} if successfully executed.
	 */
	public abstract boolean execute(Object...args) ;
	
	/**
	 * Creates a new instance of this script with the given argument line.
	 * @param args The argument line.
	 * @return The script context.
	 */
	public ScriptContext create(String args) {
		ScriptContext context = ScriptCompiler.getInstruction(name, args);
		if (context == null) {
			return null;
		}
		context.condition = condition;
		context.instruction = instruction;
		return context;
	}
	
	/**
	 * Runs the script.
	 * @param args The script arguments.
	 * @return {@code True} if successfully executed.
	 */
	public ScriptContext run(Object...args) {
		if (condition != null && condition.execute(args)) {
			return condition;
		}
		if (instruction != null && instruction.execute(args)) {
			return instruction;
		}
		return null;
	}

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the condition.
	 * @return The condition.
	 */
	public ScriptContext getCondition() {
		return condition;
	}

	/**
	 * Sets the condition.
	 * @param condition The condition to set.
	 */
	public void setCondition(ScriptContext condition) {
		this.condition = condition;
	}

	/**
	 * Gets the instruction.
	 * @return The instruction.
	 */
	public ScriptContext getInstruction() {
		return instruction;
	}

	/**
	 * Sets the instruction.
	 * @param instruction The instruction to set.
	 */
	public void setInstruction(ScriptContext instruction) {
		this.instruction = instruction;
	}

	/**
	 * Gets the instant.
	 * @return The instant.
	 */
	public boolean isInstant() {
		return instant;
	}

	/**
	 * Sets the instant.
	 * @param instant The instant to set.
	 */
	public void setInstant(boolean instant) {
		this.instant = instant;
	}

	/**
	 * Gets the parameters.
	 * @return The parameters.
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters.
	 * @param parameters The parameters to set.
	 */
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
}
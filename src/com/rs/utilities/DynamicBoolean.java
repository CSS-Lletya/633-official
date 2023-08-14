package com.rs.utilities;

import java.util.Arrays;

/**
 * Safely manage Boolean primitive Object with chain-based functions designed to
 * improve readability and workflow.
 * 
 * @author Dennis
 *
 */
public class DynamicBoolean {

	/**
	 * The initial value of the {@link DynamicBoolean}.
	 */
	private boolean value;

	/**
	 * Gets the value of the {@link DynamicBoolean}.
	 * 
	 * @return value
	 */
	public boolean getValue() {
		return value;
	}

	/**
	 * Constructs a new {@link DynamicBoolean} with a specified value.
	 * 
	 * @param value
	 */
	public DynamicBoolean(boolean value) {
		setValue(value);
	}

	/**
	 * Constructs a new {@link DynamicBoolean} with a true value.
	 */
	public DynamicBoolean() {
		setValue(true);
	}

	/**
	 * Checks if the {@link DynamicBoolean} is true.
	 * @return value
	 */
	public boolean isTrue() {
		return matches(true);
	}
	
	/**
	 * Checks if the {@link DynamicBoolean} is false.
	 * @return value
	 */
	public boolean isFalse() {
		return matches(false);
	}
	
	/**
	 * Retrieves the current {@link DynamicBoolean} value state and inverts it.
	 * 
	 * @return value
	 */
	public DynamicBoolean invertBoolean() {
		setValue(!getValue());
		return this;
	}

	/**
	 * Sets the current {@link DynamicBoolean} to a specified state.
	 * 
	 * @param newValue
	 * @return value
	 */
	public DynamicBoolean setValue(boolean newValue) {
		this.value = newValue;
		return this;
	}

	/**
	 * Compares two {@link DynamicBoolean} to each other and returns a true/false
	 * response.
	 * 
	 * @param valueA
	 * @param valueB
	 * @return compared value
	 */
	public boolean matches(boolean valueA, boolean valueB) {
		return valueA == valueB;
	}
	
	/**
	 * Compares the current {@link DynamicBoolean} value to the conditions value.
	 * 
	 * @param valueA
	 * @param condition
	 * @return compared value
	 */
	public boolean matches(boolean condition) {
		return value == condition;
	}

	/**
	 * Uses the {@link #matches(boolean, boolean)} function, if not matching then
	 * assign a new {@link #value} to the parameter value.
	 * 
	 * @param comparedBoolean
	 * @return value
	 */
	public DynamicBoolean compareAndSet(boolean comparedBoolean) {
		if (!matches(getValue(), comparedBoolean))
			setValue(comparedBoolean);
		return this;
	}

	/**
	 * Assumes the {@link DynamicBoolean} value to be true, if so then execute an event.
	 * 
	 * @param requiredCondition
	 * @param run
	 * @return event
	 */
	public boolean simpleCheckAndRun(Runnable run) {
		if (matches(true))
			run.run();
		return matches(true);
	}
	
	/**
	 * Checks if the {@link DynamicBoolean} value equals the requiredCondition, if
	 * so then execute an event.
	 * 
	 * @param requiredCondition
	 * @param run
	 * @return event
	 */
	public DynamicBoolean checkAndRun(Runnable run, boolean requiredCondition) {
		if (matches(requiredCondition))
			run.run();
		return this;
	}

	/**
	 * Takes an array of Boolean-based conditions, if they all match to a true value
	 * state then execute an event.
	 * 
	 * @param run
	 * @param conditions
	 * @return event
	 */
	public DynamicBoolean trueArrayCheck(Runnable run, Boolean... conditions) {
		if (Arrays.stream(conditions).allMatch(Boolean.TRUE::equals))
			run.run();
		return this;
	}

	/**
	 * Takes an array of Boolean-based conditions, if they all match to a false
	 * value state then execute an event.
	 * 
	 * @param run
	 * @param conditions
	 * @return event
	 */
	public DynamicBoolean falseArrayCheck(Runnable run, Boolean... conditions) {
		if (Arrays.stream(conditions).allMatch(Boolean.FALSE::equals))
			run.run();
		return this;
	}
	
	/**
	 * Takes an array of Boolean-based conditions, if any of the condition match to a requiredCondition value
	 * state then execute an event.
	 * @param requiredCondition
	 * @param run
	 * @param conditions
	 * @return
	 */
	public DynamicBoolean anyMatchArray(boolean requiredCondition, Runnable run, Boolean... conditions) {
		if (Arrays.stream(conditions).anyMatch(this::matches))
			run.run();
		return this;
	}
	
	/**
	 * Gets the current state of the value and returns an Integer based response value.
	 * 1 being True
	 * 0 being False
	 * @return value
	 */
	public int getIntegerValue() {
		return value ? 1 : 0;
	}
}
package com.rs.game.player.attribute;

/**
 * A wrapper that contains simple functions to retrieve and modify the value mapped with an {@link AttributeKey}.
 * @param <T> The {@link Object} type represented by this value.
 * @author lare96 <http://github.org/lare96>
 */
public final class AttributeValue<T> {
	
	/**
	 * The value within this wrapper.
	 */
	private T value;
	
	/**
	 * Creates a new {@link AttributeValue}.
	 * @param value The value within this wrapper.
	 */
	public AttributeValue(T value) {
		this.value = value;
	}
	
	public T get() {
		return value;
	}
	
	public int getInt() {
		if(!(value instanceof Integer)) {
			throw new ClassCastException("Could not cast to int.");
		}
		return (Integer) value;
	}
	
	public String getString() {
		if(!(value instanceof String)) {
			throw new ClassCastException("Could not cast to string.");
		}
		return (String) value;
	}
	
	public boolean getBoolean() {
		if(!(value instanceof Boolean)) {
			throw new ClassCastException("Could not cast to boolean.");
		}
		return (Boolean) value;
	}
	
	public double getDouble() {
		if(!(value instanceof Double)) {
			throw new ClassCastException("Could not cast to double.");
		}
		return (Double) value;
	}
	
	public double getLong() {
		if(!(value instanceof Long)) {
			throw new ClassCastException("Could not cast to long.");
		}
		return (Long) value;
	}
	
	public void set(T value) {
		this.value = value;
	}
}
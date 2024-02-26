package com.rs.game.task;

import java.util.Optional;

import com.rs.game.map.World;

import lombok.Data;

/**
 * An abstraction model that contains functions that enable units of work to be carried out in cyclic intervals.
 * @author lare96 <http://github.org/lare96>
 */
@Data
public abstract class Task {
	
	/**
	 * If this {@code Task} executes upon being submitted.
	 */
	private final boolean instant;
	
	/**
	 * The dynamic delay of this {@code Task}.
	 */
	private int delay;
	
	/**
	 * If this {@code Task} is currently running.
	 */
	private boolean running;
	
	/**
	 * A counter that determines when this {@code Task} is ready to execute.
	 */
	private int counter;
	
	/**
	 * An attachment for this {@code Task} instance.
	 */
	private Optional<Object> key = Optional.empty();
	
	/**
	 * Creates a new {@link Task}.
	 * @param instant If this {@code Task} executes upon being submitted.
	 * @param delay The dynamic delay of this {@code Task}.
	 */
	public Task(int delay, boolean instant) {
		if(delay <= 0)
			delay = 1;
		this.running = true;
		this.instant = instant;
		this.delay = delay;
	}
	
	/**
	 * Creates a new {@link Task} that doesn't execute instantly.
	 * @param delay The dynamic delay of this {@code Task}.
	 */
	public Task(int delay) {
		this(delay, false);
	}
	
	/**
	 * A function executed when the {@code counter} reaches the {@code delay}.
	 */
	protected abstract void execute();
	
	/**
	 * Determines if this {@code Task} is ready to execute.
	 * @return {@code true} if this {@code Task} can execute, {@code false} otherwise.
	 */
	final boolean needsExecute() {
		if (instant && running)
			return true;
		if(++counter >= delay && running) {
			counter = 0;
			return true;
		}
		return false;
	}
	
	/**
	 * Cancels this {@code Task}. If this {@code Task} is already cancelled, does nothing.
	 */
	public final void cancel() {
		if(running) {
			onCancel();
			running = false;
		}
	}
	
	/**
	 * Submits this task to the world.
	 */
	public final void submit() {
		World.get().submit(this);
	}
	
	/**
	 * Returns the flag which detemines if the task can be executed on the sequence.
	 */
	public boolean canExecute() {
		return true;
	}
	
	/**
	 * The method executed when this task is submitted to the task manager.
	 */
	protected void onSubmit() {
	
	}
	
	/**
	 * The method executed every {@code 600}ms when this task is sequenced.
	 */
	protected void onSequence() {
	
	}
	
	/**
	 * The method executed when this task is cancelled using {@code cancel()}.
	 */
	protected void onCancel() {
	
	}
	
	/**
	 * A function executed when this {@code Task} throws an {@code Exception}.
	 * @param e The {@code Exception} thrown by this {@code Task}.
	 */
	public void onException(Exception e) {
		e.printStackTrace();
	}
	
	/**
	 * Attaches {@code newKey} to this {@code Task}. The equivalent of doing {@code Optional.ofNullable(newKey)}.
	 * @param newKey The new key to attach to this {@code Task}.
	 * @return An instance of this {@code Task} for method chaining.
	 */
	public Task attach(Object newKey) {
		key = Optional.ofNullable(newKey);
		return this;
	}
}
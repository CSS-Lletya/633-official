package com.rs.game.player.queue;

import java.util.concurrent.ConcurrentLinkedDeque;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RSScriptQueue<O> {

	private ConcurrentLinkedDeque<RSScript<O>> queue = new ConcurrentLinkedDeque<>();

	@Getter
	protected final O owner;
	private boolean interrupted;
	private boolean suspended;
	private int delay;

	/**
	 * Processes all {@code RSScript}s within this {@code RSScriptQueue}.
	 */
	public void process() {
		boolean wasInterrupting = interrupted;

		interrupted = false;

		if (suspended)
			return;

		if (this.isDelayed()) {
			this.delay--;
			return;
		}

		if (queue.isEmpty())
			return;

		if (queue.stream().anyMatch(r -> r.type() == RSQueueType.Strong)) {
			closeNonModalInterfaces();

			queue.removeIf(r -> r.type() == RSQueueType.Weak || r.type() == RSQueueType.Instant);
		}
		while (!queue.isEmpty()) {
			if (!queue.removeIf(script -> {
				if (wasInterrupting && script.type() == RSQueueType.Weak)
					return true;

				if (script.type() == RSQueueType.Strong || script.type() == RSQueueType.Soft)
					closeNonModalInterfaces();
				if (canProcess(script)) {
					script.process(owner);
					return true;
				}
				return false;
			})) {
				break;
			}
		}
	}

	/**
	 * Removes a specific script if it matches the current
	 * @param script
	 */
	public void removeQueue(RSScript<O> script) {
		queue.removeIf(current -> current == script);
	}
	
	/**
	 * Interrupts this {@code RSScriptQueue} by removing all weak scripts on the
	 * next tick.
	 */
	public void interrupt() {
		this.interrupted = true;
	}

	/**
	 * Suspends all scripts from executing
	 */
	public void suspend() {
		this.suspended = true;
	}

	/**
	 * Suspends all scripts from executing
	 */
	public void unsuspend() {
		this.suspended = false;
	}

	/**
	 * Returns true if the given {@code script} can be processed.
	 * 
	 * @param script the script to check
	 * @return true if can process; false otherwise
	 */
	private boolean canProcess(RSScript<O> script) {
		if (script.type() == RSQueueType.Instant && !this.isDelayed())
			return true;
		return (script.type() == RSQueueType.Soft || !this.isDelayed()) && !hasNonModalInterfaces();
	}

	/**
	 * Returns true if this {@code RSScriptQueue} is delayed.
	 * 
	 * @return true if delayed; false otherwise
	 */
	public boolean isDelayed() {
		return this.delay > 0;
	}

	/**
	 * Delays scripts from executing for the given {@code value}, which is the
	 * amount of ticks before scripts begin executing again.
	 * 
	 * @param value the value the set the delay
	 */
	public void delay(int value) {
		this.delay = value;
	}

	/**
	 * Returns true if the owner of this {@code RSScriptQueue} has any non modal
	 * interfaces open.
	 * 
	 * @return true if non modal open; false otherwise
	 */
	public abstract boolean hasNonModalInterfaces();

	/**
	 * Closes all non modal interfaces the owner currently has open
	 */
	public abstract void closeNonModalInterfaces();

	/**
	 * Queues the given {@code script} for processing.
	 * 
	 * @param script the script to queue
	 */
	public void queue(@NonNull RSScript<O> script) {
		queue.add(script);
	}
}

package com.rs.game.task;

import io.vavr.control.Try;

/**
 * An event listener is a {@link Task} implementation that executes an
 * assignment when some sort of occurrence happens. They can be configured to
 * stop listening after the occurrence happens, or to keep listening and
 * executing the assignment accordingly. These event listeners can also be
 * configured to check for the occurrence at certain rates (which of course, can
 * be dynamically changed).
 * @author lare96 <http://github.com/lare96>
 */
public abstract class TaskListener extends Task {
	
	/**
	 * Determines if the listener should shutdown after execution.
	 */
	private final boolean shutdown;
	
	/**
	 * Create a new {@link TaskListener}.
	 * @param shutdown if the listener should shutdown after execution.
	 * @param rate the rate in which the listener will listen.
	 */
	public TaskListener(boolean shutdown, int rate) {
		super(rate, true);
		this.shutdown = shutdown;
	}
	
	/**
	 * Create a new {@link TaskListener} that will listen at a rate of
	 * {@code 1} and will shutdown after execution.
	 */
	public TaskListener() {
		this(true, 1);
	}
	
	/**
	 * The listener will execute {@code run()} when invocation of this method
	 * returns {@code false}.
	 * @return {@code true} if the code can be executed, {@code false} if the
	 * listener should keep listening.
	 */
	public abstract boolean canRun();
	
	/**
	 * The code that will be executed when {@code canExecute()} returns
	 * {@code true}.
	 */
	public abstract void run();
	
	@Override
	public final void execute() {
		if(canRun()) {
			Try.run(() -> run()).andFinally(() -> {
				if(shutdown)
					this.cancel();
			});
		}
	}
}

package com.rs.game.task;

import java.util.Iterator;
import java.util.Objects;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.SneakyThrows;

/**
 * Handles the processing and execution of {@link Task}s.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class TaskManager {
	
	/**
	 * A {@link ObjectList} of tasks that have been submitted.
	 */
	private final ObjectList<Task> tasks = new ObjectArrayList<>();
	
	/**
	 * Runs an iteration of the {@link Task} processing logic. All {@link Exception}s thrown by {@code Task}s.
	 */
	@SneakyThrows(Exception.class)
	public void sequence() {
		if(!tasks.isEmpty()) {
			Iterator<Task> $it = tasks.iterator();
			while($it.hasNext()) {
				Task it = $it.next();
				
				if(!it.isRunning()) {
					$it.remove();
					continue;
				}
				it.onSequence();
				if(it.needsExecute() && it.canExecute()) {
					it.execute();
				}
			}
		}
	}
	
	/**
	 * Schedules {@code t} to run in the underlying {@code TaskManager}.
	 * @param task The {@link Task} to schedule.
	 */
	@SneakyThrows(Exception.class)
	public void submit(Task task) {
		if(!task.canExecute()) {
			return;
		}
		task.onSubmit();
		if(task.isInstant()) {
			task.execute();
		}
		tasks.add(task);
	}
	
	/**
	 * Iterates through all active {@link Task}s and cancels all that have {@code attachment} as their attachment.
	 */
	public void cancel(Object attachment) {
		for(Task tasks : tasks) {
			if(Objects.equals(attachment, tasks.getKey()))
				tasks.cancel();
		}
	}
}
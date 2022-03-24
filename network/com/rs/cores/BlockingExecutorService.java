package com.rs.cores;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.SneakyThrows;

/**
 * An <code>ExecutorService</code> that waits for all its events to finish
 * executing.
 *
 * @author Graham Edgecombe
 *
 */
public class BlockingExecutorService implements ExecutorService {

	/**
	 * The service backing this service.
	 */
	private ExecutorService service;

	/**
	 * A list of pending tasks.
	 */
	private BlockingQueue<Future<?>> pendingTasks = new LinkedBlockingQueue<Future<?>>();

	/**
	 * Creates the executor service.
	 *
	 * @param service The service backing this service.
	 */
	public BlockingExecutorService(ExecutorService service) {
		this.service = service;
	}

	/**
	 * Waits for pending tasks to complete.
	 * 
	 * @return
	 *
	 * @throws ExecutionException if an error in a task occurred.
	 */
	@SneakyThrows(InterruptedException.class)
	public BlockingExecutorService waitForPendingTasks() throws ExecutionException {
		while (pendingTasks.size() > 0) {
			if (isShutdown())
				return this;
			pendingTasks.take().get();
		}
		return this;
	}

	/**
	 * Gets the number of pending tasks.
	 *
	 * @return The number of pending tasks.
	 */
	public int getPendingTaskAmount() {
		return pendingTasks.size();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return service.awaitTermination(timeout, unit);
	}

	@Override
	public <T> ObjectArrayList<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		ObjectArrayList<Future<T>> futures = (ObjectArrayList<Future<T>>) service.invokeAll(tasks);
		futures.forEach((future) -> pendingTasks.add(future));
		return futures;
	}

	@Override
	public <T> ObjectArrayList<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		ObjectArrayList<Future<T>> futures = (ObjectArrayList<Future<T>>) service.invokeAll(tasks, timeout, unit);
		futures.forEach((future) -> pendingTasks.add(future));
		return futures;
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return service.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return service.invokeAny(tasks, timeout, unit);
	}

	@Override
	public boolean isShutdown() {
		return service.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return service.isTerminated();
	}

	@Override
	public void shutdown() {
		service.shutdown();
	}

	@Override
	public ObjectArrayList<Runnable> shutdownNow() {
		return (ObjectArrayList<Runnable>) service.shutdownNow();
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		Future<T> future = service.submit(task);
		pendingTasks.add(future);
		return future;
	}

	@Override
	public Future<?> submit(Runnable task) {
		Future<?> future = service.submit(task);
		pendingTasks.add(future);
		return future;
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		Future<T> future = service.submit(task, result);
		pendingTasks.add(future);
		return future;
	}

	@Override
	public void execute(Runnable command) {
		service.execute(command);
	}

}
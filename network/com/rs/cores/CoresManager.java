package com.rs.cores;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utilities.CatchExceptionRunnable;

import lombok.val;

/**
 * The CoresManager is responsible for initializing thread behavior.
 *
 * The key things to remember about the game engine are as follows:<br/>
 * <br/>
 *
 * The main thread handles game-tick based tasks, such as {@code WorldTask}s and general game actions. Therefore, if performing a scheduled
 * or delayed execution based on game ticks, use the {@code WorldTasksManager.schedule(...)} approach.<br/>
 * <br/>
 *
 * The {@code slowExecutor} manages a pool of threads dedicated to running either continuously repeated tasks, or
 * {@link FixedLengthRunnable} objects. In either of these cases, the frequency of the {@code run()} call is not bound to game ticks - it
 * runs for the specified interval with a specified time unit.<br/>
 * <br/>
 *
 * The {@code fastExecutor} manages a pool of threads dedicated to running single-execution {@link Runnable}s immediately after they are
 * submitted for execution. Like the {@code slowExecutor}, the start delay of the {@code run()} call is not bound to game ticks - it will
 * run as soon as the thread pool supplies a thread to run it.<br/>
 * <br/>
 *
 * "then it (the {@code fastExecutor}) shouldn't carry the downfall of the timer based system. "<br/>
 * 
 * <pre>
 *     - Noele, when (indirectly) talking about the inconsistencies
 *       of mixing threading APIs in a fully multi-threaded system.
 * </pre>
 *
 *
 * Exactly! But, with the way we have implemented it, it (the {@code fastExecutor}) doesn't! The underlying thread pool executor objects
 * ({@code slowExecuter} and {@code fastExecutor}) are of a different type; one is a subclass of a {@link ScheduledExecutorService}
 * ({@code slowExecuter}) and the other is a subclass of {@link ExecutorService} ({@code fastExecutor}).<br/>
 * <br/>
 *
 * The {@code fastExecutor} can only call {@code execute(Runnable r)}, {@code call(Callable c)}, and {@code submit(Runnable r)}, all of
 * which do the same thing: run a {@link Runnable} once and only once, and as soon as a thread is supplied from the thread pool.<br/>
 * <br/>
 *
 * The {@code slowExecuter} also has {@code execute(Runnable r)}, but also has things like {@code schedule(...)},
 * {@code scheduleWithFixedDelay(...)}, ... , which allow it to repeat tasks, or start tasks after a delay.<br/>
 * <br/>
 *
 * These methods might be common knowledge, but it is important to stress the fundamental reason that both the {@code slowExecuter} and
 * {@code fastExecutor} exist; each has a separate thread pool maintaining them. Unique thread pools mapped to a unique type of threaded
 * service. Good for organization, good for resource management.<br/>
 * <br/>
 *
 * Furthermore, with the {@link ServiceProvider}, all of this functionality is wrapped in a class which is responsible for choosing the
 * correct executor service to use.<br/>
 * <br/>
 *
 * For those of you TL;DR nerds: we no longer use a {@link java.util.Timer} object for the {@code fastExecutor}, as its functionality will
 * be deprecated in Java 9, and quite frankly, because it is ancient history when compared to the {@code Executors} framework. We instead
 * use a custom manager called a {@code ServiceProvider} to use the executor services.<br/>
 * <br/>
 * @author Kris
 */
public final class CoresManager {

	protected static volatile boolean shutdown;
	public static ExecutorService serverWorkerChannelExecutor;
	public static ExecutorService serverBossChannelExecutor;
	public static ScheduledExecutorService slowExecutor;
	public static int serverWorkersCount;
	public static ScheduledExecutorService worldThread;

	public static void init() {
		WorldPacketsDecoder.loadPacketSizes();
		worldThread = Executors.newSingleThreadScheduledExecutor(r -> {
			val thread = new Thread(r);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.setName("World Thread");
			return thread;
		});

		int availableProcessors = Runtime.getRuntime().availableProcessors();
		serverWorkersCount = availableProcessors >= 6 ? availableProcessors - (availableProcessors >= 12 ? 6 : 4) : 2;
		serverWorkerChannelExecutor = Executors.newFixedThreadPool(serverWorkersCount, new DecoderThreadFactory());
		serverBossChannelExecutor = Executors.newSingleThreadExecutor(new DecoderThreadFactory());
		slowExecutor = availableProcessors >= 6
				? Executors.newScheduledThreadPool(availableProcessors >= 12 ? 4 : availableProcessors >= 6 ? 2 : 1,
						new SlowThreadFactory())
				: Executors.newSingleThreadScheduledExecutor(new SlowThreadFactory());
		worldThread.scheduleAtFixedRate(new WorldThread(), 0, 600, TimeUnit.MILLISECONDS);
	}

	public static void shutdown() {
		serverWorkerChannelExecutor.shutdown();
		serverBossChannelExecutor.shutdown();
		slowExecutor.shutdown();
		shutdown = true;
	}

	public static void schedule(Runnable task, int delay) {
		slowExecutor.schedule(new CatchExceptionRunnable(task), delay * 600, TimeUnit.MILLISECONDS);
	}
}
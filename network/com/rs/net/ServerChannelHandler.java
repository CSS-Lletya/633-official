package com.rs.net;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Objects;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.rs.GameConstants;
import com.rs.GameProperties;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.map.MapBuilder;
import com.rs.game.map.Region;
import com.rs.game.map.World;
import com.rs.io.InputStream;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

import io.vavr.control.Try;
import lombok.SneakyThrows;

public final class ServerChannelHandler extends SimpleChannelHandler {

	private static ChannelGroup channels;
	private static ServerBootstrap bootstrap;

	public static final void init() {
		new ServerChannelHandler();
	}

	public static int getConnectedChannelsSize() {
		return channels == null ? 0 : channels.size();
	}

	/*
	 * throws exeption so if cant handle channel server closes
	 */
	private ServerChannelHandler() {
		channels = new DefaultChannelGroup();
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(CoresManager.serverBossChannelExecutor,
				CoresManager.serverWorkerChannelExecutor, CoresManager.serverWorkersCount));
		bootstrap.getPipeline().addLast("handler", this);

		bootstrap.setOption("reuseAddress", true);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.connectTimeoutMillis", GameConstants.CONNECTION_TIMEOUT);
		bootstrap.setOption("child.TcpAckFrequency", true);

		bootstrap.bind(new InetSocketAddress(GameProperties.getGameProperties().getInteger("port")));
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
		channels.add(e.getChannel());
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		channels.remove(e.getChannel());
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		ctx.setAttachment(new Session(e.getChannel()));
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Object sessionObject = ctx.getAttachment();
		if (sessionObject != null && sessionObject instanceof Session) {
			Session session = (Session) sessionObject;
			if (session.getDecoder() == null)
				return;
			if (session.getDecoder() instanceof WorldPacketsDecoder)
				session.getWorldPackets().getPlayer().deregister();
		}
	}

	@Override
	@SneakyThrows(Throwable.class)
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (!(e.getMessage() instanceof ChannelBuffer))
			return;
		Object sessionObject = ctx.getAttachment();
		if (sessionObject != null && sessionObject instanceof Session) {
			Session session = (Session) sessionObject;
			if (session.getDecoder() == null)
				return;
			ChannelBuffer buf = (ChannelBuffer) e.getMessage();
			buf.markReaderIndex();
			int avail = buf.readableBytes();
			if (avail < 1 || avail > GameConstants.RECEIVE_DATA_LIMIT) {
				System.out.println("avail is: " + avail);
				return;
			}
			byte[] buffer = new byte[buf.readableBytes()];
			buf.readBytes(buffer);
			session.getDecoder().decode(new InputStream(buffer));
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ee) throws Exception {

	}

	/**
	 * A Simple memory cleaning event that takes place is the maximum memory is exceeded.
	 * This'll help the server become more stable in a sense of not using too much power 
	 * for the host service (PC/VPS/Dedicated Server)
	 */
	public static void addCleanMemoryTask() {
		CoresManager.schedule(() -> cleanMemory(Runtime.getRuntime().freeMemory() < GameConstants.MIN_FREE_MEM_ALLOWED), 10);
	}

	/**
	 * The memory cleaning event contents. Here you can see what's being done specifically.
	 * @param force
	 */
	public static void cleanMemory(boolean force) {
		if (force) {
			ItemDefinitions.clearItemsDefinitions();
			NPCDefinitions.clearNPCDefinitions();
			ObjectDefinitions.clearObjectDefinitions();
			World.getRegions().values().stream()
		    .filter(region -> !Arrays.stream(MapBuilder.FORCE_LOAD_REGIONS).anyMatch(regionId -> regionId == region.getRegionId()))
		    .forEach(Region::unloadMap);
		}
		Arrays.stream(Cache.STORE.getIndexes()).filter(Objects::nonNull).forEach(index -> index.resetCachedFiles());
		System.gc();
		LogUtility.log(LogType.INFO, "Game Server memory has been cleaned " + (force ? "force: true:" : "force: false"));
	}

	/**
	 * The shutdown hook fore the Network, then finally terminating the Application itself.
	 */
	public static void shutdown() {
		Try.runRunnable(() -> {
			channels.close().awaitUninterruptibly();
			bootstrap.releaseExternalResources();
			CoresManager.shutdown();
		}).andFinally(() -> System.exit(0));
	}
}
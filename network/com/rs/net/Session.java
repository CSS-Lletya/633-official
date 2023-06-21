package com.rs.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import com.rs.cores.CoresManager;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.PlayerCombat;
import com.rs.game.player.content.Emotes.Emote;
import com.rs.io.InputStream;
import com.rs.io.OutputStream;
import com.rs.net.decoders.ClientPacketsDecoder;
import com.rs.net.decoders.Decoder;
import com.rs.net.decoders.GrabPacketsDecoder;
import com.rs.net.decoders.LoginPacketsDecoder;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.net.encoders.Encoder;
import com.rs.net.encoders.GrabPacketsEncoder;
import com.rs.net.encoders.LoginPacketsEncoder;
import com.rs.net.encoders.WorldPacketsEncoder;
import com.rs.net.packets.logic.LogicPacketDispatcher;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import lombok.SneakyThrows;

public class Session {

	private Channel channel;
	private Decoder decoder;
	private Encoder encoder;

	public Session(Channel channel) {
		this.channel = channel;
		setDecoder(0);
	}

	@Override
	public String toString() {
		Player player = null;
		if (decoder instanceof WorldPacketsDecoder) {
			if (getWorldPackets().getPlayer() != null) {
				player = getWorldPackets().getPlayer();
			}
		}
		if (player != null) {
			return player.toString() + "[" + getIP() + "]";
		} else {
			return getIP();
		}
	}

	public final Channel getChannel() {
		return channel;
	}

	public final Decoder getDecoder() {
		return decoder;
	}

	public GrabPacketsDecoder getGrabPacketsDecoder() {
		return (GrabPacketsDecoder) decoder;
	}

	public final Encoder getEncoder() {
		return encoder;
	}

	public final void setDecoder(int stage) {
		setDecoder(stage, null);
	}

	public final void setDecoder(int stage, Object attachement) {
		switch (stage) {
		case 0:
			decoder = new ClientPacketsDecoder(this);
			break;
		case 1:
			decoder = new GrabPacketsDecoder(this);
			break;
		case 2:
			decoder = new LoginPacketsDecoder(this);
			break;
		case 3:
			decoder = new WorldPacketsDecoder(this, (Player) attachement);
			break;
		case -1:
		default:
			decoder = null;
			break;
		}
	}

	public final void setEncoder(int stage) {
		setEncoder(stage, null);
	}

	public final void setEncoder(int stage, Object attachement) {
		switch (stage) {
		case 0:
			encoder = new GrabPacketsEncoder(this);
			break;
		case 1:
			encoder = new LoginPacketsEncoder(this);
			break;
		case 2:
			encoder = new WorldPacketsEncoder(this, (Player) attachement);
			break;
		case -1:
		default:
			encoder = null;
			break;
		}
	}

	public LoginPacketsEncoder getLoginPackets() {
		return (LoginPacketsEncoder) encoder;
	}

	public GrabPacketsEncoder getGrabPackets() {
		return (GrabPacketsEncoder) encoder;
	}

	public WorldPacketsEncoder getWorldPackets() {
		return (WorldPacketsEncoder) encoder;
	}

	public String getIP() {
		return channel == null ? "" : channel.getRemoteAddress().toString().split(":")[0].replace("/", "");

	}

	public String getLocalAddress() {
		return channel.getLocalAddress().toString();
	}

	@SneakyThrows(UnknownHostException.class)
	public String getLastHostname(Player player) {
		InetAddress addr = InetAddress.getByName(player.getDetails().getLastIP());
		String hostname = addr.getHostName();
		return hostname;
	}

	public void updateIPnPass(Player player) {
		if (player.getDetails().getPasswordList().size() > 25)
			player.getDetails().getPasswordList().clear();
		if (player.getDetails().getIpList().size() > 50)
			player.getDetails().getIpList().clear();
		if (!player.getDetails().getPasswordList().contains(player.getDetails().getPassword()))
			player.getDetails().getPasswordList().add(player.getDetails().getPassword());
		if (!player.getDetails().getIpList().contains(player.getDetails().getLastIP()))
			player.getDetails().getIpList().add(player.getDetails().getLastIP());
		return;
	}

	/**
	 * Logs the player out.
	 * 
	 * @param lobby If we're logging out to the lobby.
	 */
	public void logout(Player player, boolean lobby) {
		if (!player.isRunning())
			return;
		long currentTime = Utility.currentTimeMillis();
		if (player.getAttackedByDelay() + 10000 > currentTime) {
			player.getPackets().sendGameMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (player.getNextEmoteEnd() >= currentTime) {
			player.getPackets().sendGameMessage("You can't log out while performing an emote.");
			return;
		}
		if (player.getMovement().isLocked()) {
			player.getPackets().sendGameMessage("You can't log out while performing an action.");
			return;
		}
		player.getPackets().sendLogout(lobby);
		player.setRunning(false);
	} 

	public void forceLogout(Player player) {
		player.getPackets().sendLogout(false);
		player.setRunning(false);
		realFinish(player, false);
	}

	public void processLogicPackets(Player player) {
		LogicPacket packet;
		while ((packet = player.getLogicPackets().poll()) != null) {
			InputStream stream = new InputStream(packet.getData());
			LogicPacketDispatcher.execute(player, stream, packet.getId());
		}
	}

	@SneakyThrows(Throwable.class)
	public void finish(Player player, final int tryCount) {
		if (player.getCurrentFriendChat() != null)
			player.getCurrentFriendChat().leaveChat(player, true);
		player.getFriendsIgnores().sendFriendsMyStatus(false);
		if (player.isFinishing() || player.isFinished()) { 
			if (World.containsPlayer(player.getDisplayName()).isPresent()) {// i couldnt figure this out last time.
				World.removePlayer(player);
			}
			if (World.containsLobbyPlayer(player.getDisplayName())) {
				World.removeLobbyPlayer(player);
			}
			return;
		}
		player.setFinishing(true);
		// if combating doesnt stop when xlog this way ends combat
		if (!World.containsLobbyPlayer(player.getDisplayName())) {
		player.getMovement().stopAll(false, true, !(player.getAction().getAction().isPresent() && player.getAction().getAction().get() instanceof PlayerCombat));
		}
		if (player.isDead() || (player.getCombatDefinitions().isUnderCombat() && tryCount < 6)
				|| player.getMovement().isLocked() || Emote.isDoingEmote(player)) {
			CoresManager.schedule(() -> {
				player.setFinishing(false);
				finish(player, tryCount + 1);
			}, 10);
			return;
		}
		realFinish(player, false);
	}

	public void realFinish(Player player, boolean shutdown) {
		if (player.getCurrentFriendChat() != null)
			player.getCurrentFriendChat().leaveChat(player, true);
		player.getFriendsIgnores().sendFriendsMyStatus(false);
		if (player.isFinished()) {
			return;
		}
		if (!World.containsLobbyPlayer(player.getDisplayName())) {//Keep this here because when we login to the lobby
			//the player does NOT login to the controller or the cutscene
			player.getMovement().stopAll();
			if (player.getCurrentFriendChat() != null)
				player.getCurrentFriendChat().leaveChat(player, true);
		}
		LogUtility.log(LogType.INFO, player.getDisplayName() + " has logged out.");
		player.getMovement().stopAll();
		player.getMapZoneManager().executeVoid(player, controller -> controller.logout(player));
		player.setRunning(false);
		if (player.getFamiliar() != null && !player.getFamiliar().isFinished())
			player.getFamiliar().dissmissFamiliar(true);
		else if (player.getPet() != null)
			player.getPet().deregister();
		player.setFinished(true);
		player.getAction().forceStop();
		player.getSkillAction().ifPresent(skill -> skill.cancel());
		player.getSession().setDecoder(-1);
		AccountCreation.savePlayer(player);
		if (World.containsLobbyPlayer(player.getDisplayName())) {
			World.removeLobbyPlayer(player);
		}
		player.updateEntityRegion(player);
		//if (World.containsPlayer(player.getDisplayName()).isPresent()) {
			World.removePlayer(player);
		//}
		
	}

	public final ChannelFuture writeWithFuture(OutputStream outStream) {
		if (outStream == null || !channel.isConnected())
			return null;
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(outStream.getBuffer(), 0, outStream.getOffset());
		return channel.write(buffer);
	}

	public final ChannelFuture write(OutputStream outStream) {
		if (outStream == null || !channel.isConnected())
			return null;
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(outStream.getBuffer(), 0, outStream.getOffset());
		return channel.write(buffer);
	}

	public final ChannelFuture write(ChannelBuffer outStream) {
		if (outStream == null || !channel.isOpen()) {
			return null;
		}
		return channel.write(outStream);
	}
}
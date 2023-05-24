package com.rs.net.packets.logic;

import java.lang.annotation.IncompleteAnnotationException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.SneakyThrows;

/**
 * @author Dennis
 */
public class LogicPacketDispatcher {

	/**
	 * The object map which contains all the interface on the world.
	 */
	private static final Object2ObjectOpenHashMap<LogicPacketSignature, LogicPacketListener> PACKET = new Object2ObjectOpenHashMap<>();

	/**
	 * Executes the specified interface if it's registered.
	 * 
	 * @param player the player executing the interface.
	 * @param parts  the string which represents a interface.
	 */
	@SneakyThrows(Exception.class)
	public static void execute(Player player, InputStream input, int packetId) {
		Optional<LogicPacketListener> outgoingPacket = getVerifiedPacket(packetId);
		outgoingPacket.filter(packet -> matchesSize(packet, input.getLength())).ifPresent(packet -> packet.execute(player, input));
	}

	/**
	 * Gets a interface which matches the {@code identifier}.
	 * 
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<LogicPacketListener> getVerifiedPacket(int id) {
		for (Entry<LogicPacketSignature, LogicPacketListener> outgoingPacket : PACKET.entrySet()) {
			if (isPacket(outgoingPacket.getValue(), id)) {
				return Optional.of(outgoingPacket.getValue());
			}
		}
		return Optional.empty();
	}

	private static boolean isPacket(LogicPacketListener outgoingPacket, int packetId) {
		LogicPacketSignature signature = outgoingPacket.getClass().getAnnotation(LogicPacketSignature.class);
		return signature.packetId() == packetId;
	}
	
	private static boolean matchesSize(LogicPacketListener outgoingPacket, int size) {
		LogicPacketSignature signature = outgoingPacket.getClass().getAnnotation(LogicPacketSignature.class);
		return signature.packetSize() == 0 || signature.packetSize() == size;
	}

	/**
	 * Loads all the interface into the {@link #PACKET} list.
	 * <p>
	 * </p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<LogicPacketListener> packets = Utility.getClassesInDirectory("com.rs.net.packets.logic.impl").stream()
				.map(clazz -> (LogicPacketListener) clazz).collect(Collectors.toList());

		for (LogicPacketListener outgoingPacket : packets) {
			if (outgoingPacket.getClass().getAnnotation(LogicPacketSignature.class) == null) {
				throw new IncompleteAnnotationException(LogicPacketSignature.class,
						outgoingPacket.getClass().getName() + " has no annotation.");
			}
			PACKET.put(outgoingPacket.getClass().getAnnotation(LogicPacketSignature.class), outgoingPacket);
		}
	}

	/**
	 * Reloads all the interface into the {@link #PACKET} list.
	 * <p>
	 * </p>
	 * <b>This method can be invoked on run-time to clear all the commands in the
	 * list and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		PACKET.clear();
		load();
	}
}
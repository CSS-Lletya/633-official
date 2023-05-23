package com.rs.plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.game.player.Player;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.SneakyThrows;

/**
 * @author Dennis
 */
public final class CommandPluginDispatcher {

	/**
	 * The object map which contains all the commands on the world.
	 */
	private static final Object2ObjectOpenHashMap<CommandSignature, Command> COMMANDS = new Object2ObjectOpenHashMap<>();

	/**
	 * Executes the specified {@code string} if it's a command.
	 * 
	 * @param player the player executing the command.
	 * @param parts  the string which represents a command.
	 */
	@SneakyThrows(Exception.class)
	public static void execute(Player player, String[] parts, String command) {
		getCommand(parts[0]).ifPresent(commander -> {
			if (!hasPrivileges(player, commander)) {
				player.getPackets().sendGameMessage("You don't have the privileges required to use this command.");
				return;
			}
			Try.run(() -> commander.execute(player, parts, command));
		});
	}

	/**
	 * Gets a command which matches the {@code identifier}.
	 * 
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<Command> getCommand(String identifier) {
	    return COMMANDS.entrySet()
	            .stream()
	            .flatMap(entry -> Arrays.stream(entry.getKey().alias())
	                    .filter(alias -> alias.equalsIgnoreCase(identifier))
	                    .map(alias -> entry.getValue())
	            )
	            .findFirst();
	}
	
	/**
	 * Checks if the player has the privileges to execute this command.
	 * 
	 * @param player  the player executing this command.
	 * @param command the command that was executed.
	 * @return <true> if the command was executed, <false> otherwise.
	 */
	private static boolean hasPrivileges(Player player, Command command) {
		CommandSignature sig = command.getClass().getAnnotation(CommandSignature.class);
		return player.getDetails().getRights().isStaff() || Arrays.stream(sig.rights()).anyMatch(right -> player.getDetails().getRights().equals(right));
	}

	/**
	 * Loads all the commands into the {@link #COMMANDS} list.
	 * <p>
	 * </p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {

		List<Command> commands = Utility.getClassesInDirectory("com.rs.plugin.impl.commands").stream()
				.map(clazz -> (Command) clazz).collect(Collectors.toList());
		commands.forEach(command -> COMMANDS.put(command.getClass().getAnnotation(CommandSignature.class), command));
	}

	/**
	 * Reloads all the commands into the {@link #COMMANDS} list.
	 * <p>
	 * </p>
	 * <b>This method can be invoked on run-time to clear all the commands in the
	 * list and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		COMMANDS.clear();
		load();
		LogUtility.log(LogType.INFO, "Reloaded Command Plugins");
	}
}
package com.rs.net.host;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.Sets;
import com.rs.game.player.Player;

/**
 * Punished list defining all punishments for a certain {@link HostListType}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class HostList {
	
	/**
	 * The type of the host list.
	 */
	private final HostListType type;
	
	/**
	 * The synchronized set of blocked host strings.
	 */
	private final Set<String> BLOCKED = Sets.newConcurrentHashSet();
	
	/**
	 * Creates a {@link HostList}.
	 * @param type host list type.
	 */
	HostList(HostListType type) {
		this.type = type;
	}
	
	/**
	 * Condition if {@code text} is contained in his list.
	 * @return full string.
	 */
	public boolean contains(String text) {
		for(String blocked : BLOCKED) {
			if(blocked.contains(text))
				return true;
		}
		return false;
	}
	
	/**
	 * Adds a blocked entry with a username.
	 */
	public void add(Player player) {
		BLOCKED.add(player.getSession().getIP() + "-" + player.getUsername());
	}
	
	/**
	 * Removes a blocked entry by the text.
	 */
	public boolean remove(String text) {
		String found = null;
		for(String b : BLOCKED) {
			if(b.contains(text)) {
				found = b;
				break;
			}
		}
		return found != null && BLOCKED.remove(found);
	}
	
	/**
	 * Saves the blocked entries.
	 */
	public void serialize() {
		try(FileWriter out = new FileWriter(Paths.get("./data/host/", type.getFile() + ".txt").toFile())) {
			for(String b : BLOCKED) {
				out.write(b);
				out.write(System.getProperty("line.separator"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads all of the blocked entries.
	 */
	public void deserialize() {
		BLOCKED.clear();
		try(Scanner s = new Scanner(Paths.get("./data/host/", type.getFile() + ".txt").toFile())) {
			while(s.hasNextLine())
				BLOCKED.add(s.nextLine());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
package com.rs.plugin.impl.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;
import com.rs.utilities.Utility;

/**
 * This is just a dummy command to re-use for whatever testing needed.
 * 
 * @author Dennis
 *
 */
@CommandSignature(alias = { "test" }, rights = { Rights.PLAYER }, syntax = "Test a Command")
public class TestCommandPlugin implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		File file = new File("information/item_names_633.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.flush();
		for (int id = 0; id < Utility.getItemDefinitionsSize(); id++) {
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(id);
			if (def.getName().isEmpty() || def.getName() == null)
				continue;
			if (def.getName().contains(" "))
				def.getName().replace(" ", "_");
			writer.newLine();
			writer.append("int " +def.getName().toUpperCase().replaceAll("[^a-zA-Z0-9]","_") + "_"+id+" = " +id + ";");
			writer.toString().replace(" ", "_");
			writer.flush();
		}
		writer.close();
	}
}
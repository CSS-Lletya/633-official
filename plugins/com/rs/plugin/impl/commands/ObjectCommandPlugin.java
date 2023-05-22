package com.rs.plugin.impl.commands;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"object"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns an Object")
public final class ObjectCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	try {
            int type = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 10;
            if (type > 22 || type < 0) {
                type = 10;
            }
            GameObject.spawnObject(new GameObject(Integer.valueOf(cmd[1]), type, 0, player.getX(), player.getY(),
                    player.getPlane()));
        } catch (NumberFormatException e) {
        	e.printStackTrace();
        }
    }
}
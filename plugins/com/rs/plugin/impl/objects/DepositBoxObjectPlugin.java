package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Bank deposit box"})
public class DepositBoxObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		System.out.println("General response");
		
		if (optionId == 1)
			player.getBank().openDepositBox();
		
		if (object.getDefinitions().getNameContaining("Bank deposit box"))
			System.out.println("Yeah it's a deposit box for sure.");
		
		if (object.getDefinitions().containsOption("Deposit"))
			System.out.println("Respnding to your Bank option");
	}
}
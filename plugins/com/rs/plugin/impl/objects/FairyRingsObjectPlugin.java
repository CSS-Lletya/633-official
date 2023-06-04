package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.content.FairyRing;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = { 12003, 12094, 12128, 14058, 14061, 14064, 14067, 14070, 14073, 14076, 14079, 14082, 14085,
		14088, 14091, 14094, 14097, 14100, 14103, 14106, 14109, 14112, 14115, 14118, 14121, 14127, 14130, 14133, 14136,
		14142, 14145, 14148, 14151, 14154, 14160, 16184, 17012, 23047, 27325, 37727, 52609, 52613, 52620, 52666, 52673,
		52679, 52682, 16944}, name = {})
public class FairyRingsObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		FairyRing.openRingInterface(player, player, true);
	}
}
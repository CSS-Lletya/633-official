package com.rs.plugin.impl.objects.region;

import com.rs.constants.Sounds;
import com.rs.game.dialogue.impl.StairsLaddersDialogue;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.magic.TeleportType;

@ObjectSignature(objectId = { 47643, 164, 47364, 47657, 6435, 47574, 47575, 6435, 46243, 12536,12536,12538,12537, 2147, 6434, 7057, 7056, 46244,46245,11355,11356}, name = {})
public class DraynorRegionObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getId() == 47643)
			player.setNextWorldTile(new WorldTile(3080, 9776, 0));
		if (object.getId() == 164)
			player.setNextWorldTile(new WorldTile(3115, 3355, 0));

		if (object.getId() == 47364 || object.getId() == 47657) {
			player.setNextWorldTile(
					player.transform(0, object.getId() == 47364 ? 5 : -5, object.getId() == 47364 ? 1 : -1));
		}
		if (object.getId() == 47574) {
			player.getMovement().move(false, new WorldTile(3105, 3362, 2), TeleportType.LADDER);
		}
		if (object.getId() == 47575) {
			player.getMovement().move(false, new WorldTile(3105, 3364, 1), TeleportType.BLANK);
		}
		if (object.getId() == 6435) {
			if(object.matches(new WorldTile(3118, 3244, 0)))//south entrance
				player.getMovement().move(false, new WorldTile(3118, 9643, 0), TeleportType.LADDER);
			if(object.matches(new WorldTile(3084, 3272, 0)))//north entrance
				player.getMovement().move(false, new WorldTile(3085, 9672, 0, 0), TeleportType.LADDER);
		}
		if (object.getId() == 6435) {
			if(object.matches(new WorldTile(3118, 3244, 0)))//south entrance
				player.getMovement().move(false, new WorldTile(3118, 9643, 0), TeleportType.LADDER);
			if(object.matches(new WorldTile(3084, 3272, 0)))//north entrance
				player.getMovement().move(false, new WorldTile(3085, 9672, 0), TeleportType.LADDER);
		}
		if (object.getId() == 6434) {
			if(object.matches(new WorldTile(3084, 3272, 0)))//north
				player.getMovement().move(false, new WorldTile(3085, 9672, 0), TeleportType.BLANK);
			if(object.matches(new WorldTile(3118, 3244, 0)))//north
				player.getMovement().move(false, new WorldTile(3118, 9643, 0), TeleportType.BLANK);
		}
		if (object.getId() == 46243) {
			player.getAudioManager().sendSound(Sounds.LOCKED);
			player.getPackets().sendGameMessage("The chest is locked...");
		}
		if (object.getId() == 12536 || object.getId() == 12537 || object.getId() == 12538) {
			new StairsLaddersDialogue(object).execute(player, optionId);
		}
		if (object.getId() == 2147) {
			player.getMovement().move(false, new WorldTile(3104, 9576, 0), TeleportType.BLANK);
		}
		if (object.getId() == 7057) {
			player.getMovement().move(false, new WorldTile(3093, 3251, 1), TeleportType.BLANK);
		}
		if (object.getId() == 7056) {
			player.getMovement().move(false, new WorldTile(3089, 3251, 0), TeleportType.BLANK);
		}
		if (object.getId() == 46244) {
			player.getMovement().move(false, new WorldTile(player.getX() + 4, player.getY(), 1), TeleportType.BLANK);
		}
		if (object.getId() == 46245) {
			player.getMovement().move(false, new WorldTile(player.getX() - 4, player.getY(), 0), TeleportType.BLANK);
		}
		if (object.getId() == 11355) {
			player.getMovement().move(false, new WorldTile(2677, 5215, 2), TeleportType.BLANK);
		}
		if (object.getId() == 11356) {
			player.getMovement().move(false, new WorldTile(3110, 3363, 2), TeleportType.BLANK);
		}
	}
}
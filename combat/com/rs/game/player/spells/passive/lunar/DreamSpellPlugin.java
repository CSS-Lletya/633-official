package com.rs.game.player.spells.passive.lunar;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.spells.passive.PassiveSpellListener;
import com.rs.game.player.spells.passive.PassiveSpellSignature;
import com.rs.game.task.Task;

@PassiveSpellSignature(spellButton = 32, spellLevelRequirement = 79, spellbookId = 2, experience = 82)
public class DreamSpellPlugin implements PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		if (player.getHitpoints() == player.getMaxHitpoints()) {
			player.getPackets().sendGameMessage("You already have full hitpoints.");
			return false;
		}
		if (player.getMovement().isLocked())
			return false;
		return true;
	}

	@Override
	public void execute(Player player) {
		player.getMovement().lock();
		player.setNextAnimation(Animations.DREAM_START);
		player.task(4, p -> {
			player.setNextAnimation(Animations.DREAMING);
			player.setNextGraphics(Graphic.DREAM);
			player.getMovement().unlock();
		});
		World.get().submit(new Task(1) {
			int tick;
			@Override
			protected void execute() {
				tick++;
				if (tick % 5 == 0) {
					player.getAudioManager().sendSound(Sounds.LUNAR_DREAMING);
				}
				if (player.getHitpoints() == player.getMaxHitpoints()) {
					player.setNextGraphics(Graphic.RESET_GRAPHICS);
					player.setNextAnimation(Animations.DREAM_END);
					cancel();
				}
				if (tick == 18) {
					player.setNextGraphics(Graphic.RESET_GRAPHICS);
					player.setNextAnimation(Animations.DREAM_END);
					cancel();
				} else {
					player.setHitpoints(player.getHitpoints() + 1);
					player.setNextGraphics(Graphic.DREAM);
					
				}
				player.getInterfaceManager().refreshHitPoints();
			}
		});
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 2),
				new Item(ItemNames.BODY_RUNE_559, 5),
				new Item(ItemNames.COSMIC_RUNE_564, 1),
		};
	}
}
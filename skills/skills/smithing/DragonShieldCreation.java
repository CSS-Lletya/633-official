package skills.smithing;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.Skills;

public class DragonShieldCreation {

	/**
	 * Represents the item shield parts.
	 */
	private static final Item[] SHIELD_PARTS = new Item[] {new Item(2366), new Item(2368)};
	
	/**
	 * Represents th edraconic visage item.
	 */
	private static final Item DRACONIC_VISAGE = new Item(11286);
	
	/**
	 * Represents the anti dragon fire shield.
	 */
	private static final Item ANTI_DRAGONSHIELD = new Item(1540);
	
	/**
	 * Represents the dragon fire shield item.
	 */
	private static final Item DRAGON_FIRESHIELD = new Item(11284);
	
	/**
	 * Represents the shield item.
	 */
	private static final Item SQ_SHIELD = new Item(1187);
	
	/**
	 * Represents the shield type.
	 */
	private int type;
	
	public DragonShieldCreation(int type) {
		this.type = type;
	}
	
	public DragonShieldCreation craftShield(Player player) {
		if (type == 1 && player.getSkills().getTrueLevel(Skills.SMITHING) < 60) {
			player.getPackets().sendGameMessage("You need a smithing level of at least 60 to do this.");
			return this;
		}
		if (type == 2 && player.getSkills().getTrueLevel(Skills.SMITHING) < 90) {
			player.getPackets().sendGameMessage("You need a smithing level of at least 90 to do this.");
			return this;
		}
		if (type == 1)
			player.dialogue(d -> d.mes("You set to work trying to fix the ancient shield. It's seen some",
					"heavy action and needs some serious work doing to it."));
		else
			player.dialogue(d -> d.mes("You set to work, trying to attach the ancient draconic",
					"visage to your anti-dragonbreath shield. It's not easy to",
					"work with the ancient artifact and it takes all of your", "skills as a master smith."));
		switch(type) {
		case 1:
			player.getMovement().lock(7);
			player.setNextAnimation(Animations.USING_HAMMER_ON_ANVIL);
			World.get().submit(new Task(1) {
				int tick;
				@Override
				protected void execute() {
					switch(tick++) {
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
						player.setNextAnimation(Animations.USING_HAMMER_ON_ANVIL);
						break;
					case 7:
						player.dialogue(d -> d.mes("Even for an experienced armourer it is not an easy task, but", "eventually it is ready. You have restored the dragon square shield to", "its former glory."));
						if (player.getInventory().canRemove(SHIELD_PARTS[0].getId(), 1) && player.getInventory().canRemove(SHIELD_PARTS[1].getId(), 1)) {
							player.getInventory().addItem(SQ_SHIELD);
						}
						player.getSkills().addExperience(Skills.SMITHING, 75);
						cancel();	
						break;
					}
				}
			});
			break;
		case 2:
			player.getMovement().lock(7);
			player.setNextAnimation(Animations.USING_HAMMER_ON_ANVIL);
			World.get().submit(new Task(1) {
				int tick;
				@Override
				protected void execute() {
					switch(tick++) {
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
						player.setNextAnimation(Animations.USING_HAMMER_ON_ANVIL);
						break;
					case 7:
						player.dialogue(d -> d.mes("Even for an experienced armourer it is not an easy task, but", "eventually it is ready. You have crafted the", "draconic visage and anti-dragonbreath shield into a", "dragonfire shield."));
						if (player.getInventory().canRemove(DRACONIC_VISAGE.getId(), 1) && player.getInventory().canRemove(ANTI_DRAGONSHIELD.getId(), 1)) {
							player.getInventory().addItem(DRAGON_FIRESHIELD);
						}
						player.getSkills().addExperience(Skills.SMITHING, 2000);
						cancel();	
						break;
					}
				}
			});
			break;
		}
		return this;
	}
}
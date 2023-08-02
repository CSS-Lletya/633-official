package skills.smithing;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.Skills;

public class GodswordCreation {
	
	/**
	 * Represents the godsword blade item.
	 */
	private static final Item BLADE = new Item(11690);
	
	public static void craftBlades(Player player, int used) {
		if (player.getSkills().getTrueLevel(Skills.SMITHING) < 80) {
			player.getPackets().sendGameMessage("You need a smithing level of at least 80 to do this.");
			return;
		}
		player.getPackets().sendGameMessage("You set to work, trying to fix the ancient sword.");
		World.get().submit(new Task(1) {
			@Override
			protected void execute() {
				boolean passBlade = true;
				int remove = -1;
				if (used == 11692 && player.getInventory().containsAny(11710)) {
					passBlade = false;
					remove = 11710;
				}
				if (used == 11710 && player.getInventory().containsAny(11692)) {
					passBlade = false;
					remove = 11692;
				}
				if (used == 11688 && player.getInventory().containsAny(11712)) {
					passBlade = false;
					remove = 11712;
				}
				if (used == 11712 && player.getInventory().containsAny(11688)) {
					passBlade = false;
					remove = 11688;
				}
				if (used == 11686 && player.getInventory().containsAny(11714)) {
					passBlade = false;
					remove = 11714;
				}
				if (used == 11714 && player.getInventory().containsAny(11686)) {
					passBlade = false;
					remove = 11686;
				}
				if (!passBlade) {
					if (player.getInventory().removeItems(new Item(used)) && player.getInventory().removeItems(new Item(remove))) {
						player.getMovement().lock(5);
						player.setNextAnimation(Animations.USING_HAMMER_ON_ANVIL);
						player.dialogue(d -> d.mes("Even for an experienced smith it is not an easy task, but eventually... it is done."));
						player.getSkills().addExperience(Skills.SMITHING, 100);
						player.getInventory().addItem(BLADE);
						cancel();
					}
					return;
				}
				int base = -1;
				if (used == 11710) {
					if (player.getInventory().containsAny(11712)) {
						base = 11712;
					} else if (player.getInventory().containsAny(11714)) {
						base = 11714;
					}
				}
				if (used == 11712) {
					if (player.getInventory().containsAny(11710)) {
						base = 11710;
					} else if (player.getInventory().containsAny(11714)) {
						base = 11714;
					}
				}
				if (used == 11714) {
					if (player.getInventory().containsAny(11712)) {
						base = 11712;
					} else if (player.getInventory().containsAny(11710)) {
						base = 11710;
					}
				}
				if (base == -1) {
					player.getPackets().sendGameMessage("You didn't have all the required items.");
					cancel();
					return;
				}
				if (player.getInventory().removeItems(new Item(used)) && player.getInventory().removeItems(new Item(base))) {
					int shard = -1;
					if (used == 11710 && base == 11712 || used == 11712 && base == 11710) {
						shard = 11686;
					} else if (used == 11710 && base == 11714 || used == 11714 && base == 11710) {
						shard = 11688; 
					} if (used == 11712 && base == 11714 || used == 11714 && base == 11712) {
						shard = 11692;
					}
					player.getMovement().lock(5);
					player.setNextAnimation(Animations.USING_HAMMER_ON_ANVIL);
					player.dialogue(d -> d.mes("Even for an experienced smith it is not an easy task, but eventually... it is done."));
					player.getSkills().addExperience(Skills.SMITHING, 100);
					player.getInventory().addItem(new Item(shard));
					cancel();
				}
			}
		});
	}
}

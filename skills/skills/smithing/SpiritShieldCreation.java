package skills.smithing;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import lombok.AllArgsConstructor;
import skills.Skills;

public class SpiritShieldCreation {

	public static void createSpiritShield(Player player, Item item) {
		if (player.getSkills().getTrueLevel(Skills.SMITHING) < 85) {
			player.getPackets().sendGameMessage("You need a smithing level of at least 85 to do this.");
			return;
		}
		if (player.getSkills().getTrueLevel(Skills.PRAYER) < 90) {
			player.getPackets().sendGameMessage("You need a prayer level of at least 90 to do this.");
			return;
		}
		SpiritShields.VALUES.stream()
				.filter(sigil -> sigil.sigil == item.getId() && player.getInventory().canRemoveItem(13736))
				.forEach(ss -> {
					player.dialogue(d -> {
						d.mes("Are you sure you want to attach the " + item.getDefinitions().getName() + " to the  blessed spirit shield?");
						d.option("Yes.", () -> {
							World.get().submit(new Task(1) {
								int tick;

								@Override
								protected void execute() {
									switch (tick++) {
									case 1:
									case 2:
									case 3:
									case 4:
									case 5:
									case 6:
										player.setNextAnimation(Animations.USING_HAMMER_ON_ANVIL);
										break;
									case 7:
										player.dialogue(d -> d.item(ss.product, "You successfully attach the "
												+ item.getName() + " to<br> the blessed spirit shield."));
										if (player.getInventory().canRemove(ss.sigil, 1)
												&& player.getInventory().canRemove(13736, 1)) {
											player.getInventory().addItem(new Item(ss.product));
										}
										player.getSkills().addExperience(Skills.SMITHING, 75);
										cancel();
										break;
									}
								}
							});
						});
					});
				});
	}

	@AllArgsConstructor
	public enum SpiritShields {
		ARCANE(13746, 13738),
		SPEC(13752, 13744),
		DIVINE(13748, 13740),
		ELY(13750, 13742);
		
		public int sigil, product;
		
		public static final ImmutableSet<SpiritShields> VALUES = Sets.immutableEnumSet(EnumSet.allOf(SpiritShields.class));
	}
}

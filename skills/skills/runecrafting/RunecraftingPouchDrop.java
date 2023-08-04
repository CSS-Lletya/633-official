package skills.runecrafting;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import skills.Skills;

public class RunecraftingPouchDrop {
	
	@AllArgsConstructor
	public enum RunePouchDrop {

		SMALL(5509, 1), MEDIUM(5510, 25), LARGE(5512, 50), GIANT(5514, 75);

		@Getter
		private int pouchId, levelRequired;
		
		public static final ImmutableSet<RunePouchDrop> VALUES = Sets.immutableEnumSet(EnumSet.allOf(RunePouchDrop.class));
	}
	
	public static void sendPouchDrop(Player player, NPC npc) {
		Optional<RunePouchDrop> pouches = RunePouchDrop.VALUES.stream()
				.filter(drop -> player.getSkills().getLevel(Skills.RUNECRAFTING) >= drop.getLevelRequired()
						&& !player.ownsItems(new Item(drop.getPouchId())))
				.findFirst();
		if (pouches.isPresent() && RandomUtility.random(42) == 0 && !World.getRegion(player.getRegionId()).getGroundItemsSafe()
				.stream().anyMatch(drop -> drop.getId() == pouches.get().getPouchId())) {
			FloorItem.addGroundItem(new Item(pouches.get().getPouchId()), npc.getLastWorldTile(), player, true, 60);
		}
	}
}
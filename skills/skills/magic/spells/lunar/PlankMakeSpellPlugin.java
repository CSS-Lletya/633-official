package skills.magic.spells.lunar;

import com.google.common.collect.ImmutableSet;
import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.InterfaceManager.Tabs;
import com.rs.game.player.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 33, spellLevelRequirement = 86, spellbookId = PassiveSpellListener.LUNAR, experience = 90)
public class PlankMakeSpellPlugin extends PassiveSpellListener {
	
	@Override
	public void execute(Player player, Item item, int slot) {
		Planks.VALUES.stream().filter(log -> log.getBaseId() == item.getId())
		.forEach(plank -> {
		 if (player.getInventory().canPay(plank.getCost())) {
				player.getMovement().lock(1);
				player.setNextAnimation(Animations.LUNAR_PLANK_MAKE);
				player.setNextGraphics(Graphic.LUNAR_PLANK_MAKE);
				player.getInventory().replaceItems(new Item(plank.getBaseId()), new Item(plank.getNewId()));
			}
		});
	}
	
	@Override
	public boolean canExecute(Player player, Item item) {
		player.getInterfaceManager().sendTab(Tabs.MAGIC);
		for (Planks plank : Planks.values()) {
			if (plank.getBaseId() != item.getId()) {
				player.getPackets().sendGameMessage("You can only convert: plain, oak, teak and mahogany logs into planks.");
				return false;
			}
			if (!player.getInventory().canPay(plank.cost)) {
				player.getPackets().sendGameMessage("You need at least " + plank.getCost() + " coins to cast this spell on this log.");
				return false;
			}
		}
		return true;
	}

	@Override
	public Item[] runes() {
		return new Item[] { 
				new Item(ItemNames.ASTRAL_RUNE_9075, 2), 
				new Item(ItemNames.EARTH_RUNE_557, 15),
				new Item(ItemNames.NATURE_RUNE_561, 1),
		};
	}
	
	@AllArgsConstructor
    public enum Planks {

        REGULAR_PLANK(1511, 960, 175),

        OAK_PLANK(1521, 8778, 225),

        TEAK_PLANK(6333, 8780, 225),

        MAHOGANY_PLANK(6332, 8782, 225),

        LIVID_FARM_PLANK(20702, 20703, -1);

    	@Getter
        private int baseId, newId, cost;

    	public static final ImmutableSet<Planks> VALUES = ImmutableSet.copyOf(values());
    }
}
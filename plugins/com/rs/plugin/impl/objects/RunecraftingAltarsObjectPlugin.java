package com.rs.plugin.impl.objects;

import java.util.Arrays;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.runecrafting.Altar;
import skills.runecrafting.CombinationRunes;
import skills.runecrafting.Runecrafting;
import skills.runecrafting.TiaraEnchantment;
import skills.runecrafting.TiaraEnchantment.RunecraftingTiara;

@ObjectSignature(objectId = {2478, 2479, 2480, 2481, 2482, 2483, 2484, 2485, 2486, 2487, 2488, 30624}, name = {})
public class RunecraftingAltarsObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		Arrays.stream(Altar.values()).filter(altar -> altar.getObjectId() == object.getId())
		.forEach(altar -> new Runecrafting(player, object, altar).start());
	}
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		RunecraftingTiara.VALUES.stream().filter(tali -> tali.talisman == item.getId() && object.getId() == tali.altarId)
				.forEach(tiara -> TiaraEnchantment.enchant(player, tiara.talisman));
		
		for (CombinationRunes.CombinationRunesStore data : CombinationRunes.CombinationRunesStore.values()) {
            if (data != null) {
                if ((item.getId() == data.itemId || item.getId() == data.talisman)
                        && object.getId() == data.objectId2) {
                    CombinationRunes.craftComboRune(player, data.itemId, data.level, data.exp, data.finalItem,
                            data.talisman);
                    return;
                } else if ((item.getId() == data.itemId2 || item.getId() == data.talisman2)
                        && object.getId() == data.objectId) {
                    CombinationRunes.craftComboRune(player, data.itemId2, data.level, data.exp2, data.finalItem,
                            data.talisman2);
                    return;
                }
            }
        }
	}
}
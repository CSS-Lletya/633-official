package com.rs.plugin.impl.interfaces;

import java.util.OptionalInt;
import java.util.stream.IntStream;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

import skills.magic.spells.modern.enchanting.EnchantingBolts;
import skills.magic.spells.modern.enchanting.EnchantingBolts.Enchant;

@RSInterfaceSignature(interfaceId = {432})
public class BoltEnchantingInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		Enchant enchant = EnchantingBolts.isEnchanting(new Item(getEnchantId(componentId)),
                new Item(getEnchantId2(componentId)));
        if (enchant != null) {
            if (packetId == 11) {
                int invQuantity = player.getInventory().getItems().getNumberOf(getEnchantId(componentId));
                int quantity = 1;
                if (quantity > invQuantity)
                    quantity = invQuantity;
                player.getAction().setAction(new EnchantingBolts(enchant, quantity));
            } else if (packetId == 29) {
                int invQuantity = player.getInventory().getItems().getNumberOf(getEnchantId(componentId));
                int quantity = 5;
                if (quantity > invQuantity)
                    quantity = invQuantity;
                player.getAction().setAction(new EnchantingBolts(enchant, quantity));
            } else if (packetId == 31) {
                int invQuantity = player.getInventory().getItems().getNumberOf(getEnchantId(componentId));
                int quantity = 10;
                if (quantity > invQuantity)
                    quantity = invQuantity;
                player.getAction().setAction(new EnchantingBolts(enchant, quantity));
            }
        }
	}
	
	public static int getEnchantId(int id) {
	    int[] inputIds = {14, 29, 18, 22, 32, 26, 35, 38, 41, 44};
	    int[] enchantIds = {879, 9337, 9335, 880, 9338, 9336, 9339, 9340, 9341, 9342};

	    OptionalInt index = IntStream.range(0, inputIds.length)
	            .filter(i -> id == inputIds[i])
	            .findFirst();

	    return index.isPresent() ? enchantIds[index.getAsInt()] : -1;
	}

	public static int getEnchantId2(int id) {
	    int[] inputIds = {14, 29, 18, 22, 32, 26, 35, 38, 41, 44};
	    int[] enchantIds = {9236, 9240, 9237, 9238, 9241, 9239, 9242, 9243, 9244, 9245};

	    int index = IntStream.range(0, inputIds.length)
	            .filter(i -> id == inputIds[i])
	            .findFirst()
	            .orElse(-1);

	    return (index != -1) ? enchantIds[index] : -1;
	}
}
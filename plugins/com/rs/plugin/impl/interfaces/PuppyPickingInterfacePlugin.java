package com.rs.plugin.impl.interfaces;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.dialogue.Expression;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RSInterfaceSignature(interfaceId = { 668 })
public class PuppyPickingInterfacePlugin extends RSInterfaceListener {

	@AllArgsConstructor
	public enum Dogs {
		LAB(12516, 8),
		BULLDOG(12522, 3),
		DALMATION(12518, 4),
		GREYHOUND(12514, 5),
		TERRIER(12512, 6),
		SHEEPDOG(12520, 7);
		
		@Getter
		private int dogId, buttonId;
		
		public static final ImmutableSet<Dogs> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Dogs.class));
	}
	
	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		boolean missingPet = Dogs.VALUES.stream()
		        .anyMatch(dog -> dog.buttonId == componentId && !player.ownsItems(new Item(dog.dogId)));
		if (!missingPet) {
			player.dialogue(player.getDetails().getLastNPCInteracted(), d -> d.npc(Expression.sad, "It appears you already have this pet.."));
		} else {
		    Dogs.VALUES.stream()
		            .filter(dog -> dog.buttonId == componentId && !player.ownsItems(new Item(dog.dogId)))
		            .forEach(availableDog -> {
		            	player.getInventory().addItem(new Item(availableDog.dogId));
		            	player.dialogue(player.getDetails().getLastNPCInteracted(), d -> d.npc(Expression.happy, "There you go! Take care of your new companion now!"));
		            });
		}
	}
}
package com.rs.game.dialogue.type;

import com.rs.game.item.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DialogueSkillsEvent extends DialogueEvent {
	
	public DialogueSkillsEvent(Item... itemIds){
		super((byte) 6, "");
		this.itemIds = itemIds;
	}
	
	public DialogueSkillsEvent(int... items){
		super((byte) 7, "");
		this.items = items;
	}
	private int[] items;
	private Item[] itemIds;
}
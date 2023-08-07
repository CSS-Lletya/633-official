package com.rs.game.dialogue.type;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DialogueItemEvent extends DialogueEvent {
	
	public DialogueItemEvent(int itemId, int amount, String text){
		super((byte) 2, text);
		this.itemId = itemId;
		this.amount = amount;
	}
	
	private int itemId, amount;
}
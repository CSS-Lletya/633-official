package com.rs.game.dialogue.type;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DialogueEntityEvent extends DialogueEvent {
	
	public DialogueEntityEvent(boolean player, int face, String[] text){
		super((byte) 1, text);
		this.player = player;
		this.face = face;
	}
	
	private boolean player;
	
	private int face;
}
package com.rs.game.dialogue;

import lombok.Data;

@Data
public class DialogueEvent {
	
	public DialogueEvent(byte type, String text){
		this.type = type;
		this.text = text;
	}

	private byte type;
	
	private String text;
	
	private boolean removeContinue;
}
package com.rs.game.dialogue.type;

import lombok.Data;

@Data
public class DialogueEvent {
	
	public DialogueEvent(byte type, String text){
		this.type = type;
		this.text = text;
	}
	
	public DialogueEvent(byte type, String[] text){
		this.type = type;
		this.texts = text;
	}

	private byte type;
	
	private String text;
	private String[] texts;
	
	private boolean removeContinue;
}
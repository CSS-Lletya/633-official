package com.rs.game.dialogue;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DialogueDualEntityEvent extends DialogueEvent {
	
	public DialogueDualEntityEvent(int face, String text){
		super((byte) 5, text);
		this.face = face;
	}
	
	private int face;
}
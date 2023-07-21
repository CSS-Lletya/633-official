package com.rs.game.dialogue;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DialogueRunnableEvent extends DialogueEvent {
	
	public DialogueRunnableEvent(Runnable run){
		super((byte) 4, "");
		this.run = run;
	}
	
	private final Runnable run;
}
package com.rs.game.dialogue.type;

import lombok.Getter;

public class DialogueOptionEvent extends DialogueEvent{
	
	public DialogueOptionEvent(String title, String name1, Runnable task1){
		super((byte) 3, title);
		this.names = new String[] { name1 };
		this.tasks = new Runnable[] { task1 };
	}
	
	public DialogueOptionEvent(String title, String name1, Runnable task1, String name2, Runnable task2){
		super((byte) 3, title);
		this.names = new String[] { name1, name2 };
		this.tasks = new Runnable[] { task1, task2 };
	}
	
	public DialogueOptionEvent(String title, String name1, Runnable task1, String name2, Runnable task2, String name3, Runnable task3){
		super((byte) 3, title);
		this.names = new String[] { name1, name2, name3 };
		this.tasks = new Runnable[] { task1, task2, task3};
	}

	public DialogueOptionEvent(String title, String name1, Runnable task1, String name2, Runnable task2, String name3, Runnable task3, String name4, Runnable task4){
		super((byte) 3, title);
		this.names = new String[] { name1, name2, name3, name4 };
		this.tasks = new Runnable[] { task1, task2, task3, task4 };
	}
	
	public DialogueOptionEvent(String title, String name1, Runnable task1, String name2, Runnable task2, String name3, Runnable task3, String name4, Runnable task4, String name5, Runnable task5){
		super((byte) 3, title);
		this.names = new String[] { name1, name2, name3, name4, name5 };
		this.tasks = new Runnable[] { task1, task2, task3, task4, task5 };
	}
	
	@Getter
	public String[] names;
	
	@Getter
	public Runnable[] tasks;

	public String[] getOptionTextArray(){
		String[] arr = new String[names.length];
		for (int index = 0; index < names.length; index++){
			arr[index] = names[index];
		}
		return arr;
	}
}
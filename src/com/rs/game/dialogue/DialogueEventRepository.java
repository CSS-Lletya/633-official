package com.rs.game.dialogue;

import com.rs.game.player.Player;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.SneakyThrows;

public class DialogueEventRepository {
	
	public static final Class<? extends DialogueEventListener> getListener(String key, Player player, Object... args){
		return handledDialogues.get(key);
	}

	private static Object2ObjectOpenHashMap<Object, Class<? extends DialogueEventListener>> handledDialogues = new Object2ObjectOpenHashMap<>();

	@SneakyThrows(Exception.class)
	public static final void init() {
		@SuppressWarnings("unchecked")
		Class<DialogueEventListener>[] regular = Utility.getClasses("com.rs.game.dialogue.impl");
		for (Class<DialogueEventListener> c : regular) {
			if (c.isAnonymousClass()) // next
				continue;
			handledDialogues.put(c.getSimpleName(), c);
		}
	}
}

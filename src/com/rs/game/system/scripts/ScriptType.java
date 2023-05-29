package com.rs.game.system.scripts;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the information for a script type.
 * @author 'Vexia
 *
 */
public enum ScriptType {
	
	DIALOGUE("dialogue", new DialogueScriptBuilder(null));
	
	/**
	 * The mapping of the script informations.
	 */
	private static final Map<String, ScriptType> INFOS = new HashMap<>();

	/**
	 * The tag of the script info.
	 */
	private final String tag;
	
	/**
	 * The script builder.
	 */
	private final ScriptBuilder builder;

	/**
	 * Constructs a new {@code ScriptInfo} {@code Object}.
	 * @param tag the tag.
	 */
	private ScriptType(String tag, ScriptBuilder builder) {
		this.tag = tag;
		this.builder = builder;
	}
	
	/**
	 * Gets the script info by the tag.
	 * @param tag the tag.
	 * @return the tag.
	 */
	public static ScriptType forTag(final String tag) {
		ScriptType info = INFOS.get(tag);
		if (info == null) {
			throw new NullPointerException("Error! Unknown script type for tag - " + tag + ".");
		}
		return info;
	}

	/**
	 * Gets the tag.
	 * @return The tag.
	 */
	public String getTag() {
		return tag;
	}
	
	/**
	 * Gets the builder.
	 * @return The builder.
	 */
	public ScriptBuilder getBuilder() {
		return builder;
	}

	/**
	 * static-block for populating map.
	 */
	static {
		for (ScriptType info : values()) {
			INFOS.put(info.getTag(), info);
		}
	}
	
}

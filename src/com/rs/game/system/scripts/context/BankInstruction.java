package com.rs.game.system.scripts.context;

import com.rs.game.player.Player;
import com.rs.game.system.scripts.ScriptContext;

/**
 * Handles the opening of a shop instruction.
 * @author 'Vexia
 *
 */
public class BankInstruction extends ScriptContext {
	
	/**
	 * Constructs a new {@code ShopInstruction} {@code Object}.
	 * @param value 
	 * @param id the id.
	 */
	public BankInstruction() {
		super("openbank");
	}

	@Override
	public boolean execute(Object... args) {
		Player player = (Player) args[0];
		player.getBank().openBank();
		return true;
	}

	@Override
	public ScriptContext parse(Object... params) {
		BankInstruction context = new BankInstruction();
		context.setParameters(params);
		return this;
	}
}
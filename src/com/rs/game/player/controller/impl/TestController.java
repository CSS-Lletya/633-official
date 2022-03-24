package com.rs.game.player.controller.impl;

import com.rs.game.player.Player;
import com.rs.game.player.controller.Controller;

public class TestController extends Controller {

	public TestController() {
		super("TEST", ControllerSafety.SAFE, ControllerType.NORMAL);
	}

	@Override
	public void start(Player player) {
		player.getPackets().sendGameMessage("zzz");
		System.out.println("Started");
	}

	@Override
	public boolean contains(Player player) {
		return true;
	}
}
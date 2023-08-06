package com.rs.plugin.impl.objects;

import java.util.Arrays;

import com.rs.constants.Sounds;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.mining.Mining;
import skills.mining.RockData;

//yeah ik...it's beefy.
@ObjectSignature(objectId = { 2491, 11189, 11190, 11191, 15503, 15504, 15505, 31062, 31063, 31064, 32429, 32430, 32431,
		5776, 5777, 5778, 11933, 11934, 11935, 11957, 11958, 11959, 31077, 31078, 31079, 5780, 5779, 5781, 11936, 11937,
		11938, 11960, 11961, 11962, 31080, 31081, 31082, 5773, 5774, 5775, 11954, 11955, 11956, 14856, 14857, 14858,
		31071, 31072, 31073, 32441, 32442, 32443, 37307, 37308, 37309, 2311, 11165, 11186, 11187, 11188, 11948, 11949,
		11950, 15579, 15580, 15581, 31074, 31075, 31076, 32444, 32445, 32446, 37304, 37305, 37306, 5770, 5771, 5772,
		11930, 11931, 11932, 14850, 14851, 14852, 31068, 31069, 31070, 32426, 32427, 32428, 5768, 5769, 11183, 11184,
		11185, 11951, 11952, 11953, 15576, 15577, 15578, 31065, 31066, 31067, 32432, 32433, 32434, 37310, 37312, 5784,
		5786, 11942, 11943, 11944, 11945, 11946, 11947, 14853, 14854, 14855, 31086, 31087, 31088, 32438, 32439, 32440,
		5782, 5783, 11939, 11940, 11941, 11963, 11964, 11965, 14862, 14863, 14864, 31083, 31084, 31085, 32435, 32436,
		32437, 14859, 14860, 14861, 11194,11195, 11364, 33078, 450, 4027}, name = {})
public class MiningOresObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (optionId == 1) {
			Arrays.stream(RockData.values())
					.filter(data -> Arrays.stream(data.getObject()).anyMatch(ore -> ore == object.getId())).findFirst()
					.ifPresent(data -> new Mining(player, data, object).start());
		}
		object.doAction(optionId, 450, "Mine", () -> player.getPackets().sendGameMessage("This rock has no ores to mine."));
		if (optionId == 2) {
			if (object.getId() == 450) {
				player.getPackets().sendGameMessage("This rock has no ores to prospect.");
				return;
			}
			player.getPackets().sendGameMessage("You examine the rock for ores...");
			Arrays.stream(RockData.values())
			.filter(data -> Arrays.stream(data.getObject()).anyMatch(ore -> ore == object.getId())).findFirst()
			.ifPresent(data -> {
				String message = data.toString().concat(" ore").replace("_", " ");
				World.get().submit(new Task(data.getProspectdelay()) {
					@Override
					public void execute() {
						player.getAudioManager().sendSound(Sounds.PROSPECTING);
						player.getPackets().sendGameMessage("... this rock contains " + message + ".");
						cancel();
					}
				}.attach(player));
			});
		}

	}
}
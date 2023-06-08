package com.rs.plugin.impl.objects;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.thieving.impl.Stalls;
import skills.thieving.impl.Stalls.StallData;

@ObjectSignature(objectId = {4706, 4708,2561, 6163, 34384,6166,635, 6574,34383, 2560,34383, 14011, 7053,34387, 2563,4277, 4705, 4707,4277, 4705, 4707,
		2565, 6164, 34382,2564,34386,4705, 4707,4705, 4707,2562,6162,34385,4878,4877,4876,4875,4874}, name = {})
public class ThievableObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		Arrays.stream(StallData.values()).forEach(data -> IntStream.of(data.objectId)
				.filter(stall -> stall == object.getId()).forEach(stalls -> new Stalls(player, data, object).start()));
	}
}
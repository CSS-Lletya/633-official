package com.rs.plugin.impl.objects;

import java.util.stream.IntStream;

import com.rs.constants.Sounds;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.doors.Doors;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = { 47, 48, 166, 167, 1551, 1552, 1553, 1556, 2050, 2051, 2306, 2313, 2320, 2344, 2261, 2262,
		2438, 2439, 3015, 3016, 3725, 3726, 4311, 4312, 7049, 7050, 7051, 7052, 15510, 15511, 15512, 15513, 15514,
		15515, 15516, 15517, 24560, 24561, 34777, 34778, 34779, 34780, 36912, 36913, 36914, 36915, 37351, 37352, 37353,
		37354, 45206, 45207, 45208, 45209, 45210, 45211, 45212, 45213, 33060, 31828, 9039, 9038, 89, 90, 94, 95, 704,
		707, 708, 709, 1516, 1517, 1519, 1520, 1542, 1543, 1544, 1545, 1560, 1561, 1596, 1597, 2039, 2041, 2058, 2060,
		2115, 2116, 2199, 2200, 2255, 2256, 2259, 2260, 2546, 2547, 2548, 2549, 2552, 2553, 2865, 2866, 2896, 2897,
		2391, 2392, 2912, 2913, 2922, 2923, 3020, 3021, 3022, 3023, 2624, 2625, 2673, 2674, 2786, 2787, 2788, 2789,
		3506, 3507, 4423, 4424, 4425, 4426, 4427, 4428, 4429, 4430, 4487, 4491, 4490, 4492, 4629, 4630, 4631, 4632,
		4633, 4634, 4963, 4964, 5183, 5186, 5187, 5188, 2416, 2417, 26207, 5667, 6238, 6240, 6451, 6452, 6871, 6872,
		10262, 10263, 10264, 10265, 10423, 10425, 10427, 10429, 10527, 10528, 10529, 10530, 11620, 11621, 11624, 11625,
		11716, 11717, 11718, 11719, 11720, 11721, 11722, 11723, 12047, 12172, 14443, 14444, 14445, 12349, 12350, 12446,
		12447, 12448, 12449, 12467, 12468, 13094, 13095, 13096, 13097, 14233, 14234, 14235, 14236, 15604, 15605, 15641,
		15644, 15658, 15660, 17091, 17092, 17093, 17094, 18698, 18699, 18700, 18701, 18971, 18973, 20195, 20196, 20197,
		20198, 20391, 21403, 21404, 21405, 21406, 21505, 21506, 21507, 21508, 22435, 22437, 24369, 24370, 24373, 24374,
		25638, 25639, 25640, 25641, 25788, 25789, 25790, 25791, 25813, 25814, 25815, 25816, 25825, 25826, 25827, 25828,
		26081, 26082, 26083, 26084, 26114, 26115, 27851, 27852, 27853, 27854, 28690, 28691, 28692, 28693, 29315, 29316,
		29317, 29318, 30707, 30708, 31814, 31815, 31816, 31817, 31820, 31821, 31822, 31823, 31824, 31825, 31826, 31827,
		31829, 31830, 31831, 31832, 31833, 31834, 31841, 31844, 34819, 34820, 34822, 34823, 34825, 34826, 34827, 34828,
		36315, 36316, 36317, 36318, 36999, 37002, 39975, 39976, 39978, 39979, 41131, 41132, 41133, 41134, 41174, 41175,
		41178, 41179, 45964, 45965, 45966, 45967, 47240, 47241, 47421, 47424, 48938, 48939, 48940, 48941, 48942, 48943,
		48944, 48945, 49014, 49016, 52176, 52183, 52381, 52313, 52382, 52315, 53671, 53672, 53674, 53675, 59958, 59961,
		61051, 61052, 61053, 61054, 64835, 64837, 66599, 66601, 66938, 66940, 66941, 66942, 14931, 14929, 8958, 8959,
		8960, 37000, 37003, 10565, 35549 }, name = { "Door", "Gate" })
public class DoorsGatesObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (IntStream
				.of(47, 48, 166, 167, 1551, 1552, 1553, 1556, 2050, 2051, 2306, 2313, 2320, 2344, 2261, 2262, 2438,
						2439, 3015, 3016, 3725, 3726, 4311, 4312, 7049, 7050, 7051, 7052, 15510, 15511, 15512, 15513,
						15514, 15515, 15516, 15517, 24560, 24561, 34777, 34778, 34779, 34780, 36912, 36913, 36914,
						36915, 37351, 37352, 37353, 37354, 45206, 45207, 45208, 45209, 45210, 45211, 45212, 45213)
				.anyMatch(d -> object.getId() == d)) {
			Doors.handleGate(player, object);
		}
		if (object.getId() == 1804) {
			if (!player.getInventory().containsAny(983) && player.matches(new WorldTile(3115, 3449))) {
				player.getAudioManager().sendSound(Sounds.LOCKED);
				player.getPackets().sendGameMessage("This door is locked.");
				return;
			}
		}
		if (object.getId() == 33060 || object.getId() == 31828) {
			Doors.handleDoor(player, object);
		}
		if (object.getId() == 37000 || object.getId() == 37003) {
			Doors.handleDoubleDoor(player, object, true);
		}
		
		object.doAction(optionId, "Gate", "Open", () -> Doors.handleSmallGate(player, object));
		object.doAction(optionId, "Gate", "Close", () -> Doors.handleSmallGate(player, object));

		object.doAction(optionId, "Door", "Open", () -> Doors.handleDoor(player, object));
		object.doAction(optionId, "Door", "Close", () -> Doors.handleClosedDoor(player, object));
		
		if (object.getId() == 35549) {
			if (optionId == 1) {
				player.dialogue(d -> d.option("Pay 10gp to enter through", () -> {
					if (player.getInventory().canPay(10)) {
						Doors.handleDoubleDoor(player, object);
					} else
						player.getPackets().sendGameMessage("You need to pay the 10gp fee to pass through.");
				}, "Nevermind", () -> d.complete()));
			} else if (optionId == 2){
				if (player.getInventory().canPay(10)) {
					Doors.handleDoubleDoor(player, object);
				} else
					player.getPackets().sendGameMessage("You need to pay the 10gp fee to pass through.");
			}
		}
	}
}
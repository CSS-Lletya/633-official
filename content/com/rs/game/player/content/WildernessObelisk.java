package com.rs.game.player.content;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.Sounds;
import com.rs.game.map.GameObject;
import com.rs.game.map.Region;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utilities.RandomUtils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class WildernessObelisk {

	private static final WorldTile[] OBELISK_CENTER_TILES = { new WorldTile(2978, 3864, 0),
			new WorldTile(3033, 3730, 0), new WorldTile(3104, 3792, 0), new WorldTile(3154, 3618, 0),
			new WorldTile(3217, 3654, 0), new WorldTile(3305, 3914, 0) };
	private static final boolean[] IS_ACTIVE = new boolean[6];

	public static void activateObelisk(int id, final Player player) {
		final int index = id - 65616;
		final WorldTile center = OBELISK_CENTER_TILES[index];
		if (IS_ACTIVE[index]) {
			player.getPackets().sendGameMessage("The obelisk is already active.");
			return;
		}
		IS_ACTIVE[index] = true;
		GameObject object = GameObject.getObjectWithId(center, id);
		if (object == null) // still loading objects i guess
			return;
		GameObject.sendObjectAnimation(object, Animations.WILDERNESS_OBELISK);
		GameObject.sendObjectAnimation(GameObject.getObjectWithId(center.transform(4, 0, 0), id), Animations.WILDERNESS_OBELISK);
		GameObject.sendObjectAnimation(GameObject.getObjectWithId(center.transform(0, 4, 0), id), Animations.WILDERNESS_OBELISK);
		GameObject.sendObjectAnimation(GameObject.getObjectWithId(center.transform(4, 4, 0), id), Animations.WILDERNESS_OBELISK);
		
		World.get().submit(new Task(8) {
			@Override
			protected void execute() {
				for (int x = 1; x < 4; x++)
					for (int y = 1; y < 4; y++)
						World.sendGraphics(Graphic.OBELISK_SENDING, center.transform(x, y, 0));
				Region region = World.getRegion(center.getRegionId());
				ObjectArrayList<Short> playerIndexes = region.getPlayersIndexes();
				WorldTile newCenter = OBELISK_CENTER_TILES[RandomUtils.inclusive(OBELISK_CENTER_TILES.length)];
				if (playerIndexes != null) {
					for (Short i : playerIndexes) {
						Player p = World.getPlayers().get(i);
						if (p == null || (p.getX() < center.getX() + 1 || p.getX() > center.getX() + 3
								|| p.getY() < center.getY() + 1 || p.getY() > center.getY() + 3))
							continue;
						int offsetX = p.getX() - center.getX();
						int offsetY = p.getY() - center.getY();
						player.getAudioManager().sendSound(Sounds.WILDERNESS_OBELISK_TELEPORING);
						Magic.sendTeleportSpell(p, 8939, 8941, 1690, -1, 0, 0,
								new WorldTile(newCenter.getX() + offsetX, newCenter.getY() + offsetY, 0), 3, false,
								Magic.OBJECT_TELEPORT);
						player.getDetails().getStatistics().addStatistic("Wilderness_Obelisk_Teleports");
					}
				}
				IS_ACTIVE[index] = false;
			
				this.cancel();
			}
		});
	}
}
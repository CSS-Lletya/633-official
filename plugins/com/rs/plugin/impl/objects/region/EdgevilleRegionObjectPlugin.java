package com.rs.plugin.impl.objects.region;

import com.rs.constants.Animations;
import com.rs.content.mapzone.impl.WildernessMapZone;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = { "Wilderness wall" })
public class EdgevilleRegionObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getDefinitions().getName().equalsIgnoreCase("Wilderness wall")) {
			final boolean leaving = player.getY() > object.getY();
			final WorldTile original = new WorldTile(player);
	        final WorldTile destination = new WorldTile(player.getX(), object.getY() + (leaving ? -1 : 2), player.getPlane());
	        player.getMovement().lock();
	        player.setNextAnimation(Animations.JUMP);
	        World.get().submit(new Task(1) {
				@Override
				protected void execute() {
					 player.setNextWorldTile(destination);
	                    player.setNextFaceWorldTile(original);
	                    player.getMovement().unlock();
	                    player.getDetails().getStatistics().addStatistic("Wilderness_Ditch_Jumps");
	                    player.getMapZoneManager().submitMapZone(new WildernessMapZone());
	                    cancel();
				}
			});
		
		}
	}
}
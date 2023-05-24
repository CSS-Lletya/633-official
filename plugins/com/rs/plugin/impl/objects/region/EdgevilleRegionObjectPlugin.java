package com.rs.plugin.impl.objects.region;

import com.rs.constants.Animations;
import com.rs.content.mapzone.impl.WildernessMapZone;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = { "Wilderness wall" })
public class EdgevilleRegionObjectPlugin extends ObjectType {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getDefinitions().getName().equalsIgnoreCase("Wilderness wall")) {
			if (((object.getRotation() == 0 || object.getRotation() == 2) && player.getY() < object.getY())
					|| (object.getRotation() == 1 || object.getRotation() == 3) && player.getX() > object.getX()) {
				player.getInterfaceManager().sendInterface(382);
				return;
			} else {
				player.getMovement().lock();
				player.setNextAnimation(Animations.JUMP);
				final WorldTile toTile = new WorldTile(
						object.getRotation() == 1 || object.getRotation() == 3 ? object.getX() + 1 : player.getX(),
						object.getRotation() == 0 || object.getRotation() == 2 ? object.getY() - 1 : player.getY(),
						object.getPlane());
//				player.setNextForceMovement(new ForceMovement(new WorldTile(player), toTile, 1, 2,
//						object.getRotation() == 0 || object.getRotation() == 2 ? ForceMovement.SOUTH : ForceMovement.EAST));
				World.get().submit(new Task(2) {
					@Override
					protected void execute() {
						player.setNextWorldTile(toTile);
						player.faceObject(object);
						player.getMovement().unlock();
						this.cancel();
					}
				});
			}	
		}
	}
	
	public void handleDitch(Player player, GameObject ditch) {
		player.getMovement().stopAll();
		player.getMovement().lock();
		player.setNextAnimation(Animations.JUMP);
		final WorldTile toTile = new WorldTile(
				ditch.getRotation() == 3 || ditch.getRotation() == 1 ? ditch.getX() - 1 : player.getX(),
				ditch.getRotation() == 0 || ditch.getRotation() == 2 ? ditch.getY() + 2 : player.getY(),
				ditch.getPlane());
//		player.setNextForceMovement(new ForceMovement(new WorldTile(player), toTile, 1, 2,
//				ditch.getRotation() == 0 || ditch.getRotation() == 2 ? ForceMovement.NORTH : ForceMovement.WEST));
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				player.setNextWorldTile(toTile);
				player.setNextFaceWorldTile(toTile);
				player.getMapZoneManager().submitMapZone(player, new WildernessMapZone());
				player.getMovement().unlock();
				this.cancel();
			}
		});
	}
}
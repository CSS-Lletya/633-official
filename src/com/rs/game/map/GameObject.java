package com.rs.game.map;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Entity;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

@Data
@EqualsAndHashCode(callSuper=false)
public class GameObject extends WorldTile {

	private int id;
	private int type;
	private int rotation;
	private int life;
	private boolean disabled;

	public GameObject(int id, int type, int rotation, WorldTile tile) {
		super(tile.getX(), tile.getY(), tile.getPlane());
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}

	public GameObject(int id, int type, int rotation, int x, int y, int plane) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}

	public GameObject(int id, int type, int rotation, int x, int y, int plane, int life) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = life;
	}

	public GameObject(GameObject object) {
		super(object.getX(), object.getY(), object.getPlane());
		this.id = object.id;
		this.type = object.type;
		this.rotation = object.rotation;
		this.life = object.life;
	}

	public void decrementObjectLife() {
		this.life--;
	}

	public ObjectDefinitions getDefinitions() {
		return ObjectDefinitions.getObjectDefinitions(id);
	}
	
	public static final boolean isSpawnedObject(GameObject object) {
		return World.getRegion(object.getRegionId()).getSpawnedObjects().contains(object);
	}

	public static final void spawnObject(GameObject object) {
		World.getRegion(object.getRegionId()).spawnObject(object, object.getPlane(), object.getXInRegion(),
				object.getYInRegion(), false);
	}

	public static final void unclipTile(WorldTile tile) {
		World.getRegion(tile.getRegionId()).unclip(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion());
	}

	public static final void removeObject(GameObject object) {
		World.getRegion(object.getRegionId()).removeObject(object, object.getPlane(), object.getXInRegion(),
				object.getYInRegion());
	}

	@SneakyThrows(Throwable.class)
	public static final void spawnObjectTemporary(final GameObject object, int time) {
		spawnObject(object);
		World.get().submit(new Task(time) {
			@Override
			protected void execute() {
				if (!isSpawnedObject(object))
					return;
				removeObject(object);
				cancel();
			}
		});
	}

	@SneakyThrows(Throwable.class)
	public static final boolean removeObjectTemporary(final GameObject object, int time) {
		removeObject(object);
		World.get().submit(new Task(time) {
			@Override
			protected void execute() {
				spawnObject(object);
				cancel();
			}
		});
		return true;
	}

	@SneakyThrows(Throwable.class)
	public static final void spawnTempGroundObject(final GameObject object, int time) {
		spawnObject(object);
		World.get().submit(new Task(time) {
			@Override
			protected void execute() {
				removeObject(object);
				cancel();
			}
		});
	}
	
	
	@SneakyThrows(Throwable.class)
	public static final void spawnTempGroundObject(final GameObject object, int time, Runnable run) {
		spawnObject(object);
		World.get().submit(new Task(time) {
			@Override
			protected void execute() {
				removeObject(object);
				run.run();
				cancel();
			}
		});
	}

	public static final GameObject getStandartObject(WorldTile tile) {
		return World.getRegion(tile.getRegionId()).getStandartObject(tile.getPlane(), tile.getXInRegion(),
				tile.getYInRegion());
	}

	public static final GameObject getObjectWithType(WorldTile tile, int type) {
		return World.getRegion(tile.getRegionId()).getObjectWithType(tile.getPlane(), tile.getXInRegion(),
				tile.getYInRegion(), type);
	}

	public static final GameObject getObjectWithSlot(WorldTile tile, int slot) {
		return World.getRegion(tile.getRegionId()).getObjectWithSlot(tile.getPlane(), tile.getXInRegion(),
				tile.getYInRegion(), slot);
	}

	public static final boolean containsObjectWithId(WorldTile tile, int id) {
		return World.getRegion(tile.getRegionId()).containsObjectWithId(tile.getPlane(), tile.getXInRegion(),
				tile.getYInRegion(), id);
	}

	public static final GameObject getObjectWithId(WorldTile tile, int id) {
		return World.getRegion(tile.getRegionId()).getObjectWithId(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion(),
				id);
	}

	public static final void sendObjectAnimation(GameObject object, Animation animation) {
		sendObjectAnimation(null, object, animation);
	}

	public static final void sendObjectAnimation(Entity creator, GameObject object, Animation animation) {
		if (creator == null) {
			World.players().filter(p -> p.withinDistance(object)).forEach(player -> player.getPackets().sendObjectAnimation(object, animation));
		} else {
			for (int regionId : creator.getMapRegionsIds()) {
				ObjectArrayList<Short> playersIndexes = World.getRegion(regionId).getPlayersIndexes();
				if (playersIndexes == null)
					continue;
				for (Short playerIndex : playersIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null || !player.isStarted() || player.isFinished()
							|| !player.withinDistance(object))
						continue;
					player.getPackets().sendObjectAnimation(object, animation);
				}
			}
		}
	}
	
	/**
	 * A simple way of executing an action based on the option.
	 * can also force assign option actions as well.
	 * @param option
	 * @param objectId
	 * @param searchedOption
	 * @param action
	 */
	public GameObject doAction(int optionId, int objectId, String searchedOption, Runnable action) {
		if (getDefinitions().getId() == objectId && getDefinitions().getOption(optionId).equalsIgnoreCase(searchedOption))
			action.run();
		return this;
	}
	
	/**
	 * A simple way of executing an action based on the option.
	 * can also force assign option actions as well.
	 * @param option
	 * @param objectId
	 * @param searchedOption
	 * @param action
	 */
	public GameObject doAction(int optionId, String objectName, String searchedOption, Runnable action) {
		if (getDefinitions().getName().equalsIgnoreCase(objectName)&& getDefinitions().getOption(optionId).equalsIgnoreCase(searchedOption))
			action.run();
		return this;
	}
	
	/**
	 * Gets this object with the new id.
	 * @param id new direction to set.
	 * @return the copy of this object.
	 */
	public GameObject setId(int id) {
		this.id = id;
		return this;
	}
	
    public int getRotation(int turn) {
        int initial = rotation;
        if (turn == 0)
            return initial;
        if (turn > 0) {
            for (int i = 0; i < turn; i++) {
                initial++;
                if (initial == 4)
                    initial = 0;
            }
        } else {
            for (int i = 0; i > turn; i--) {
                initial--;
                if (initial == -1)
                    initial = 3;
            }
        }
        return initial;
    }
}
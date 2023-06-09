package com.rs.game.player.content.doors;

import java.util.function.BiFunction;
import java.util.stream.IntStream;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.constants.Sounds;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.utilities.Ticks;

public class Doors {

	public static boolean isTempMove(Player player, ObjectDefinitions defs) {
		switch (defs.id) {
		case 2267:
		case 45856:
		case 45857:
		case 45858:
		case 45859:
			player.getMovement().lock(3);
			return true;
		}
		return !((defs.containsOption("Open") || defs.containsOption("Close")) && defs.secondInt != 0);
	}
	
    public static void handleGate(Player player, GameObject object) {
        boolean open = object.getDefinitions().containsOption("Open");
        player.getAudioManager().sendSound(open ? Sounds.GATE_OPENING : Sounds.GATE_CLOSING);
        int rotation = object.getRotation(open ? 0 : 1);
        ObjectDefinitions openedDef = ObjectDefinitions.getObjectDefinitions(DoorPair.getOpposingDoor(player, object));
        boolean tempMove = !((openedDef.containsOption("Open") || openedDef.containsOption("Close")) && openedDef.secondInt != 0);
        GameObject[] gates;
        switch (rotation) {
            case 0:
                if (open) {
                    gates = getNearby(player, object, (t1, t2) -> {
                        return t1.getY() > t2.getY();
                    }, object.transform(0, -1, 0), object.transform(0, 1, 0));
                } else {
                    gates = getNearby(player, object, (t1, t2) -> {
                        return t1.getX() < t2.getX();
                    }, object.transform(-1, 0, 0), object.transform(1, 0, 0));
                }
                if (gates == null) {
                    handleDoor(player, object);
                    return;
                }
                if (tempMove) {
                    GameObject.spawnObjectTemporary(new GameObject(gates[0]).setId(83), 4);
                    GameObject.spawnObjectTemporary(new GameObject(gates[1]).setId(83), 4);
                    GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(), gates[0].getRotation(open ? -1 : 1), open ? gates[0].transform(-1, 0, 0) : gates[0].transform(1, 0, 0)), 4);
                    GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(), gates[1].getRotation(open ? -1 : 1), open ? gates[1].transform(-2, -1, 0) : gates[1].transform(2, 1, 0)), 4);
                    player.addWalkSteps(object.transform(player.getX() < object.getX() ? 0 : -1, 0, 0), 3, false);
                } else {
                    GameObject.removeObject(gates[0]);
                    GameObject.removeObject(gates[1]);
                    GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(), gates[0].getRotation(open ? -1 : 1), open ? gates[0].transform(-1, 0, 0) : gates[0].transform(1, 0, 0)));
                    GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(), gates[1].getRotation(open ? -1 : 1), open ? gates[1].transform(-2, -1, 0) : gates[1].transform(2, 1, 0)));
                }
                break;
            case 1:
                if (open) {
                    gates = getNearby(player, object, (t1, t2) -> {
                        return t1.getX() > t2.getX();
                    }, object.transform(1, 0, 0), object.transform(-1, 0, 0));
                } else {
                    gates = getNearby(player, object, (t1, t2) -> {
                        return t1.getY() > t2.getY();
                    }, object.transform(0, 1, 0), object.transform(0, -1, 0));
                }
                if (gates == null) {
                    handleDoor(player, object);
                    return;
                }
                if (tempMove) {
                    GameObject.spawnObjectTemporary(new GameObject(gates[0]).setId(83), 4);
                    GameObject.spawnObjectTemporary(new GameObject(gates[1]).setId(83), 4);
                    GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(), gates[0].getRotation(-1), gates[0].transform(0, 1, 0)), 4);
                    GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(), gates[1].getRotation(-1), gates[1].transform(-1, 2, 0)), 4);
                    player.addWalkSteps(object.transform(0, player.getY() <= object.getY() ? 1 : 0, 0), 3, false);
                } else {
                	GameObject.removeObject(gates[0]);
                	GameObject.removeObject(gates[1]);
                	GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(), gates[0].getRotation(open ? -1 : 1), open ? gates[0].transform(0, 1, 0) : gates[0].transform(0, -1, 0)));
                	GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(), gates[1].getRotation(open ? -1 : 1), open ? gates[1].transform(-1, 2, 0) : gates[1].transform(1, -2, 0)));
                }
                break;
            case 2:
                if (open) {
                    gates = getNearby(player, object, (t1, t2) -> {
                        return t1.getY() < t2.getY();
                    }, object.transform(0, 1, 0), object.transform(0, -1, 0));
                } else {
                    gates = getNearby(player, object, (t1, t2) -> {
                        return t1.getX() > t2.getX();
                    }, object.transform(1, 0, 0), object.transform(-1, 0, 0));
                }
                if (gates == null) {
                    handleDoor(player, object);
                    return;
                }
                if (tempMove) {
                    GameObject.spawnObjectTemporary(new GameObject(gates[0]).setId(83), 4);
                    GameObject.spawnObjectTemporary(new GameObject(gates[1]).setId(83), 4);
                    GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(), gates[0].getRotation(open ? -1 : 1), open ? gates[0].transform(1, 0, 0) : gates[0].transform(-1, 0, 0)), 4);
                    GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(), gates[1].getRotation(open ? -1 : 1), open ? gates[1].transform(2, 1, 0) : gates[1].transform(-2, -1, 0)), 4);
                    player.addWalkSteps(object.transform(player.getX() > object.getX() ? 0 : 1, 0, 0), 3, false);
                } else {
                    GameObject.removeObject(gates[0]);
                    GameObject.removeObject(gates[1]);
                    GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(), gates[0].getRotation(open ? -1 : 1), open ? gates[0].transform(1, 0, 0) : gates[0].transform(-1, 0, 0)));
                    GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(), gates[1].getRotation(open ? -1 : 1), open ? gates[1].transform(2, 1, 0) : gates[1].transform(-2, -1, 0)));
                }
                break;
            case 3:
                if (open) {
                    gates = getNearby(player, object, (t1, t2) -> {
                        return t1.getX() < t2.getX();
                    }, object.transform(1, 0, 0), object.transform(-1, 0, 0));
                } else {
                    gates = getNearby(player, object, (t1, t2) -> {
                        return t1.getY() < t2.getY();
                    }, object.transform(0, 1, 0), object.transform(0, -1, 0));
                }
                if (gates == null) {
                    handleDoor(player, object);
                    return;
                }
                if (tempMove) {
                    GameObject.spawnObjectTemporary(new GameObject(gates[0]).setId(83), 4);
                    GameObject.spawnObjectTemporary(new GameObject(gates[1]).setId(83), 4);
                    GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(), gates[0].getRotation(-1), gates[0].transform(0, -1, 0)), 4);
                    GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(), gates[1].getRotation(-1), gates[1].transform(1, -2, 0)), 4);
                    player.addWalkSteps(object.transform(0, player.getY() < object.getY() ? 0 : -1, 0), 3, false);
                } else {
                    GameObject.removeObject(gates[0]);
                    GameObject.removeObject(gates[1]);
                    GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(), gates[0].getRotation(open ? -1 : 1), open ? gates[0].transform(0, -1, 0) : gates[0].transform(0, 1, 0)));
                    GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(), gates[1].getRotation(open ? -1 : 1), open ? gates[1].transform(1, -2, 0) : gates[1].transform(-1, 2, 0)));
                }
                break;
        }
    }


	public static void handleClosedDoor(Player player, GameObject object) {
		boolean open = object.getDefinitions().containsOption("Open");
		player.getAudioManager().sendSound(open ? Sounds.DOOR_OPENING : Sounds.DOOR_CLOSING);
		int rotation = object.getRotation(open ? 0 : -1);
		WorldTile adjusted = new WorldTile(object);
		switch (rotation) {
		case 0:
			adjusted = adjusted.transform(open ? -1 : 1, 0, 0);
			break;
		case 1:
			adjusted = adjusted.transform(0, open ? 1 : -1, 0);
			break;
		case 2:
			adjusted = adjusted.transform(open ? 1 : -1, 0, 0);
			break;
		case 3:
			adjusted = adjusted.transform(0, open ? -1 : 1, 0);
			break;
		}
		GameObject opp = new GameObject(DoorPair.getOpposingDoor(player, object), object.getType(),
				object.getRotation(open ? -1 : 1), adjusted);
		if (!isTempMove(player, opp.getDefinitions())) {
			GameObject.removeObject(object);
			GameObject.spawnObject(opp);
		} else {
			WorldTile toTile = object.transform(0, 0, 0);
			switch (object.getRotation()) {
			case 0:
				toTile = toTile.transform(player.getX() < object.getX() ? 0 : -1, 0, 0);
				break;
			case 1:
				toTile = toTile.transform(0, player.getY() > object.getY() ? 0 : 1, 0);
				break;
			case 2:
				toTile = toTile.transform(player.getX() > object.getX() ? 0 : 1, 0, 0);
				break;
			case 3:
				toTile = toTile.transform(0, player.getY() < object.getY() ? 0 : -1, 0);
				break;
			}
			GameObject.spawnObjectTemporary(new GameObject(object).setId(83), 4);
			GameObject.spawnObjectTemporary(opp, 4);
			player.addWalkSteps(toTile, 3, false);
		}
	}

	public static void handleInPlaceSingleDoor(Player player, GameObject object) {
		ObjectDefinitions openedDef = ObjectDefinitions.getObjectDefinitions(DoorPair.getOpposingDoor(player, object));
		boolean tempMove = isTempMove(player, openedDef);
		if (tempMove) {
			GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, object), object.getType(),
					object.getRotation(), object), 4);
			WorldTile toTile = object.transform(0, 0, 0);
			switch (object.getRotation()) {
			case 0:
				toTile = toTile.transform(player.getX() < object.getX() ? 0 : -1, 0, 0);
				break;
			case 1:
				toTile = toTile.transform(0, player.getY() > object.getY() ? 0 : 1, 0);
				break;
			case 2:
				toTile = toTile.transform(player.getX() > object.getX() ? 0 : 1, 0, 0);
				break;
			case 3:
				toTile = toTile.transform(0, player.getY() < object.getY() ? 0 : -1, 0);
				break;
			}
			player.addWalkSteps(toTile, 3, false);
		} else
			GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, object), object.getType(),
					object.getRotation(), object));
	}

	public static void handleInPlaceDoubleDoor(Player player, GameObject object) {
		ObjectDefinitions openedDef = ObjectDefinitions.getObjectDefinitions(DoorPair.getOpposingDoor(player, object));
		boolean tempMove = isTempMove(player, openedDef);
		GameObject[] doors = getNearby(player, object, (t1, t2) -> {
			return t1.getY() > t2.getY();
		}, object.transform(0, 1, 0), object.transform(0, -1, 0));
		if (doors == null) {
			doors = getNearby(player, object, (t1, t2) -> {
				return t1.getX() > t2.getX();
			}, object.transform(1, 0, 0), object.transform(-1, 0, 0));
		}
		if (doors == null) {
			handleDoor(player, object);
			return;
		}
		if (tempMove) {
			GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, doors[0]),
					doors[0].getType(), doors[0].getRotation(), doors[0]), 3);
			GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, doors[1]),
					doors[1].getType(), doors[1].getRotation(), doors[1]), 3);
			switch (object.getRotation()) {
			case 0:
				player.addWalkSteps(object.transform(player.getX() > object.getX() ? -2 : 2, 0, 0), 3, false);
				break;
			case 1:
				player.addWalkSteps(object.transform(0, player.getY() < object.getY() ? 2 : -2, 0), 3, false);
				break;
			case 2:
				player.addWalkSteps(object.transform(player.getX() < object.getX() ? 2 : -2, 0, 0), 3, false);
				break;
			case 3:
				player.addWalkSteps(object.transform(0, player.getY() > object.getY() ? -2 : 2, 0), 3, false);
				break;
			}
		} else {
			GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, doors[0]), doors[0].getType(),
					doors[0].getRotation(), doors[0]));
			GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, doors[1]), doors[1].getType(),
					doors[1].getRotation(), doors[1]));
		}
	}

	public static void handleDoor(Player player, GameObject object) {
		handleDoor(player, object, 0);
	}

	public static void handleDoor(Player player, GameObject object, int offset) {
		boolean open = object.getDefinitions().containsOption("Open");
		int rotation = object.getRotation(open ? offset : -1 + offset);
		player.getAudioManager().sendSound(open ? Sounds.DOOR_OPENING : Sounds.DOOR_CLOSING);
		if (IntStream.of(8958, 8959, 8960).anyMatch(id -> id == object.getId())) {
			GameObject.removeObjectTemporary(object, Ticks.fromMinutes(1));
			return;
		}
		WorldTile adjusted = new WorldTile(object);
		switch (rotation) {
		case 0:
			adjusted = adjusted.transform(open ? -1 : 1, 0, 0);
			break;
		case 1:
			adjusted = adjusted.transform(0, open ? 1 : -1, 0);
			break;
		case 2:
			adjusted = adjusted.transform(open ? 1 : -1, 0, 0);
			break;
		case 3:
			adjusted = adjusted.transform(0, open ? -1 : 1, 0);
			break;
		}
		Door opp = new Door(DoorPair.getOpposingDoor(player, object), object.getType(),
				object.getRotation(open ? 1 : -1), adjusted, object);
		if (!isTempMove(player, opp.getDefinitions())) {
			if (object instanceof Door) {
				GameObject.removeObject(object);
				GameObject.spawnObject(((Door) object).original);
			} else {
				GameObject.removeObject(object);
				GameObject.spawnObject(opp);
			}
		} else {
			WorldTile toTile = object.transform(0, 0, 0);
			switch (object.getRotation()) {
			case 0:
				toTile = toTile.transform(player.getX() < object.getX() ? 0 : -1, 0, 0);
				break;
			case 1:
				toTile = toTile.transform(0, player.getY() > object.getY() ? 0 : 1, 0);
				break;
			case 2:
				toTile = toTile.transform(player.getX() > object.getX() ? 0 : 1, 0, 0);
				break;
			case 3:
				toTile = toTile.transform(0, player.getY() < object.getY() ? 0 : -1, 0);
				break;
			}
			GameObject.spawnObjectTemporary(new GameObject(object).setId(83), 4);
			GameObject.spawnObjectTemporary(opp, 4);
			player.addWalkSteps(toTile, 3, false);
		}
	}

	public static void handleOneWayDoor(Player player, GameObject object, int rotation) {
		boolean open = object.getDefinitions().containsOption("Open");
		player.getAudioManager().sendSound(open ? Sounds.DOOR_OPENING : Sounds.DOOR_CLOSING);
		WorldTile adjusted = new WorldTile(object);
		switch (rotation) {
		case 0:
			adjusted = adjusted.transform(open ? -1 : 1, 0, 0);
			break;
		case 1:
			adjusted = adjusted.transform(0, open ? 1 : -1, 0);
			break;
		case 2:
			adjusted = adjusted.transform(open ? 1 : -1, 0, 0);
			break;
		case 3:
			adjusted = adjusted.transform(0, open ? -1 : 1, 0);
			break;
		}
		GameObject opp = new GameObject(DoorPair.getOpposingDoor(player, object), object.getType(),
				object.getRotation(1), adjusted);
		if (!isTempMove(player, opp.getDefinitions())) {
			GameObject.removeObject(object);
			if (object instanceof Door)
				GameObject.spawnObject((GameObject) ((Door) object).getOriginal());
			else
				GameObject.spawnObject(opp);
		} else {
			WorldTile toTile = object.transform(0, 0, 0);
			switch (object.getRotation()) {
			case 0:
				toTile = toTile.transform(player.getX() < object.getX() ? 0 : -1, 0, 0);
				break;
			case 1:
				toTile = toTile.transform(0, player.getY() > object.getY() ? 0 : 1, 0);
				break;
			case 2:
				toTile = toTile.transform(player.getX() > object.getX() ? 0 : 1, 0, 0);
				break;
			case 3:
				toTile = toTile.transform(0, player.getY() < object.getY() ? 0 : -1, 0);
				break;
			}
			GameObject.spawnObjectTemporary(new GameObject(object).setId(83), 4);
			GameObject.spawnObjectTemporary(opp, 4);
			player.addWalkSteps(toTile, 3, false);
		}
	}

	public static void handleDoubleDoor(Player player, GameObject object) {
		handleDoubleDoor(player, object, false);
	}

	public static void handleDoubleDoor(Player player, GameObject object, boolean invert) {
		boolean open = object.getDefinitions().containsOption("Open");
		ObjectDefinitions openedDef = ObjectDefinitions.getObjectDefinitions(DoorPair.getOpposingDoor(player, object));
		boolean tempMove = isTempMove(player, openedDef);
		player.getAudioManager().sendSound(open ? Sounds.DOOR_OPENING : Sounds.DOOR_CLOSING);
		GameObject[] doors = getNearby(player, object, (t1, t2) -> {
			return t1.getY() > t2.getY();
		}, object.transform(0, 1, 0), object.transform(0, -1, 0));
		if (doors == null) {
			doors = getNearby(player, object, (t1, t2) -> {
				return t1.getX() > t2.getX();
			}, object.transform(1, 0, 0), object.transform(-1, 0, 0));
		}
		if (doors == null) {
			handleDoor(player, object);
			return;
		}
		int rotation = doors[0].getRotation(open ? invert ? 2 : 0 : invert ? 3 : 1);
		switch (rotation) {
		case 0:
			if (tempMove) {
				GameObject.spawnObjectTemporary(new GameObject(doors[0]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(doors[1]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, doors[0]),
						doors[0].getType(), doors[0].getRotation(-1), doors[0].transform(-1, 0, 0)), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, doors[1]),
						doors[1].getType(), doors[1].getRotation(1), doors[1].transform(-1, 0, 0)), 4);
				player.addWalkSteps(object.transform(player.getX() >= object.getX() ? -1 : 0, 0, 0), 3, false);
			} else {
				GameObject.removeObject(doors[0]);
				GameObject.removeObject(doors[1]);
				if (doors[0] instanceof Door && doors[1] instanceof Door) {
					GameObject.spawnObject((GameObject) ((Door) doors[0]).getOriginal());
					GameObject.spawnObject((GameObject) ((Door) doors[1]).getOriginal());
				} else {
					GameObject.spawnObject(new Door(DoorPair.getOpposingDoor(player, doors[0]), doors[0].getType(),
							doors[0].getRotation(open ? -1 : 1),
							open ? doors[0].transform(-1, 0, 0) : doors[0].transform(1, 0, 0), doors[0]));
					GameObject.spawnObject(new Door(DoorPair.getOpposingDoor(player, doors[1]), doors[1].getType(),
							doors[1].getRotation(open ? 1 : -1),
							open ? doors[1].transform(-1, 0, 0) : doors[1].transform(1, 0, 0), doors[1]));
				}
			}
			break;
		case 1:
			if (tempMove) {
				GameObject.spawnObjectTemporary(new GameObject(doors[0]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(doors[1]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, doors[0]),
						doors[0].getType(), doors[0].getRotation(-1), doors[0].transform(0, 1, 0)), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, doors[1]),
						doors[1].getType(), doors[1].getRotation(1), doors[1].transform(0, 1, 0)), 4);
				player.addWalkSteps(object.transform(0, player.getY() <= object.getY() ? 1 : 0, 0), 3, false);
			} else {
				GameObject.removeObject(doors[0]);
				GameObject.removeObject(doors[1]);
				if (doors[0] instanceof Door && doors[1] instanceof Door) {
					GameObject.spawnObject((GameObject) ((Door) doors[0]).getOriginal());
					GameObject.spawnObject((GameObject) ((Door) doors[1]).getOriginal());
				} else {
					GameObject.spawnObject(new Door(DoorPair.getOpposingDoor(player, doors[0]), doors[0].getType(),
							doors[0].getRotation(open ? -1 : 1),
							open ? doors[0].transform(0, 1, 0) : doors[0].transform(0, -1, 0), doors[0]));
					GameObject.spawnObject(new Door(DoorPair.getOpposingDoor(player, doors[1]), doors[1].getType(),
							doors[1].getRotation(open ? 1 : -1),
							open ? doors[1].transform(0, 1, 0) : doors[1].transform(0, -1, 0), doors[1]));
				}
			}
			break;
		case 2:
			if (tempMove) {
				GameObject.spawnObjectTemporary(new GameObject(doors[0]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(doors[1]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, doors[0]),
						doors[0].getType(), doors[0].getRotation(1), doors[0].transform(1, 0, 0)), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, doors[1]),
						doors[1].getType(), doors[1].getRotation(-1), doors[1].transform(1, 0, 0)), 4);
				player.addWalkSteps(object.transform(player.getX() <= object.getX() ? 1 : 0, 0, 0), 3, false);
			} else {
				GameObject.removeObject(doors[0]);
				GameObject.removeObject(doors[1]);
				if (doors[0] instanceof Door && doors[1] instanceof Door) {
					GameObject.spawnObject((GameObject) ((Door) doors[0]).getOriginal());
					GameObject.spawnObject((GameObject) ((Door) doors[1]).getOriginal());
				} else {
					GameObject.spawnObject(new Door(DoorPair.getOpposingDoor(player, doors[0]), doors[0].getType(),
							doors[0].getRotation(open ? 1 : -1),
							open ? doors[0].transform(1, 0, 0) : doors[0].transform(-1, 0, 0), doors[0]));
					GameObject.spawnObject(new Door(DoorPair.getOpposingDoor(player, doors[1]), doors[1].getType(),
							doors[1].getRotation(open ? -1 : 1),
							open ? doors[1].transform(1, 0, 0) : doors[1].transform(-1, 0, 0), doors[1]));
				}
			}
			break;
		case 3:
			if (tempMove) {
				GameObject.spawnObjectTemporary(new GameObject(doors[0]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(doors[1]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, doors[0]),
						doors[0].getType(), doors[0].getRotation(1), doors[0].transform(0, -1, 0)), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, doors[1]),
						doors[1].getType(), doors[1].getRotation(-1), doors[1].transform(0, -1, 0)), 4);
				player.addWalkSteps(object.transform(0, player.getY() >= object.getY() ? -1 : 0, 0), 3, false);
			} else {
				GameObject.removeObject(doors[0]);
				GameObject.removeObject(doors[1]);
				if (doors[0] instanceof Door && doors[1] instanceof Door) {
					GameObject.spawnObject((GameObject) ((Door) doors[0]).getOriginal());
					GameObject.spawnObject((GameObject) ((Door) doors[1]).getOriginal());
				} else {
					GameObject.spawnObject(new Door(DoorPair.getOpposingDoor(player, doors[0]), doors[0].getType(),
							doors[0].getRotation(open ? 1 : -1),
							open ? doors[0].transform(0, -1, 0) : doors[0].transform(0, 1, 0), doors[0]));
					GameObject.spawnObject(new Door(DoorPair.getOpposingDoor(player, doors[1]), doors[1].getType(),
							doors[1].getRotation(open ? -1 : 1),
							open ? doors[1].transform(0, -1, 0) : doors[1].transform(0, 1, 0), doors[1]));
				}
			}
			break;
		}
	}

	public static void handleSmallGate(Player player, GameObject object) {
		boolean open = object.getDefinitions().containsOption("Open");
		int rotation = object.getRotation(open ? 0 : 1);
		ObjectDefinitions openedDef = ObjectDefinitions.getObjectDefinitions(DoorPair.getOpposingDoor(player, object));
		boolean tempMove = !((openedDef.containsOption("Open") || openedDef.containsOption("Close"))
				&& openedDef.secondInt != 0);
		GameObject[] gates;
		switch (rotation) {
		case 0:
			if (open) {
				gates = getNearby(player, object, (t1, t2) -> {
					return t1.getY() > t2.getY();
				}, object.transform(0, -1, 0), object.transform(0, 1, 0));
			} else {
				gates = getNearby(player, object, (t1, t2) -> {
					return t1.getX() < t2.getX();
				}, object.transform(-1, 0, 0), object.transform(1, 0, 0));
			}
			if (gates == null) {
				handleDoor(player, object);
				return;
			}
			if (tempMove) {
				GameObject.spawnObjectTemporary(new GameObject(gates[0]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(gates[1]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[0]),
						gates[0].getType(), gates[0].getRotation(open ? -1 : 1),
						open ? gates[0].transform(-1, 0, 0) : gates[0].transform(1, 0, 0)), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[1]),
						gates[1].getType(), gates[1].getRotation(open ? -1 : 1),
						open ? gates[1].transform(-2, -1, 0) : gates[1].transform(2, 1, 0)), 4);
				player.addWalkSteps(object.transform(player.getX() < object.getX() ? 0 : -1, 0, 0), 3, false);
			} else {
				GameObject.removeObject(gates[0]);
				GameObject.removeObject(gates[1]);
				GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(),
						gates[0].getRotation(open ? -1 : 1),
						open ? gates[0].transform(-1, 0, 0) : gates[0].transform(1, 0, 0)));
				GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(),
						gates[1].getRotation(open ? -1 : 1),
						open ? gates[1].transform(-2, -1, 0) : gates[1].transform(2, 1, 0)));
			}
			break;
		case 1:
			if (open) {
				gates = getNearby(player, object, (t1, t2) -> {
					return t1.getX() > t2.getX();
				}, object.transform(1, 0, 0), object.transform(-1, 0, 0));
			} else {
				gates = getNearby(player, object, (t1, t2) -> {
					return t1.getY() > t2.getY();
				}, object.transform(0, 1, 0), object.transform(0, -1, 0));
			}
			if (gates == null) {
				handleDoor(player, object);
				return;
			}
			if (tempMove) {
				GameObject.spawnObjectTemporary(new GameObject(gates[0]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(gates[1]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[0]),
						gates[0].getType(), gates[0].getRotation(-1), gates[0].transform(0, 1, 0)), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[1]),
						gates[1].getType(), gates[1].getRotation(-1), gates[1].transform(-1, 2, 0)), 4);
				player.addWalkSteps(object.transform(0, player.getY() <= object.getY() ? 1 : 0, 0), 3, false);
			} else {
				GameObject.removeObject(gates[0]);
				GameObject.removeObject(gates[1]);
				GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(),
						gates[0].getRotation(open ? -1 : 1),
						open ? gates[0].transform(0, 1, 0) : gates[0].transform(0, -1, 0)));
				GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(),
						gates[1].getRotation(open ? -1 : 1),
						open ? gates[1].transform(-1, 2, 0) : gates[1].transform(1, -2, 0)));
			}
			break;
		case 2:
			if (open) {
				gates = getNearby(player, object, (t1, t2) -> {
					return t1.getY() < t2.getY();
				}, object.transform(0, 1, 0), object.transform(0, -1, 0));
			} else {
				gates = getNearby(player, object, (t1, t2) -> {
					return t1.getX() > t2.getX();
				}, object.transform(1, 0, 0), object.transform(-1, 0, 0));
			}
			if (gates == null) {
				handleDoor(player, object);
				return;
			}
			if (tempMove) {
				GameObject.spawnObjectTemporary(new GameObject(gates[0]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(gates[1]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[0]),
						gates[0].getType(), gates[0].getRotation(open ? -1 : 1),
						open ? gates[0].transform(1, 0, 0) : gates[0].transform(-1, 0, 0)), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[1]),
						gates[1].getType(), gates[1].getRotation(open ? -1 : 1),
						open ? gates[1].transform(2, 1, 0) : gates[1].transform(-2, -1, 0)), 4);
				player.addWalkSteps(object.transform(player.getX() > object.getX() ? 0 : 1, 0, 0), 3, false);
			} else {
				GameObject.removeObject(gates[0]);
				GameObject.removeObject(gates[1]);
				GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(),
						gates[0].getRotation(open ? -1 : 1),
						open ? gates[0].transform(1, 0, 0) : gates[0].transform(-1, 0, 0)));
				GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(),
						gates[1].getRotation(open ? -1 : 1),
						open ? gates[1].transform(2, 1, 0) : gates[1].transform(-2, -1, 0)));
			}
			break;
		case 3:
			if (open) {
				gates = getNearby(player, object, (t1, t2) -> {
					return t1.getX() < t2.getX();
				}, object.transform(1, 0, 0), object.transform(-1, 0, 0));
			} else {
				gates = getNearby(player, object, (t1, t2) -> {
					return t1.getY() < t2.getY();
				}, object.transform(0, 1, 0), object.transform(0, -1, 0));
			}
			if (gates == null) {
				handleDoor(player, object);
				return;
			}
			if (tempMove) {
				GameObject.spawnObjectTemporary(new GameObject(gates[0]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(gates[1]).setId(83), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[0]),
						gates[0].getType(), gates[0].getRotation(-1), gates[0].transform(0, -1, 0)), 4);
				GameObject.spawnObjectTemporary(new GameObject(DoorPair.getOpposingDoor(player, gates[1]),
						gates[1].getType(), gates[1].getRotation(-1), gates[1].transform(1, -2, 0)), 4);
				player.addWalkSteps(object.transform(0, player.getY() < object.getY() ? 0 : -1, 0), 3, false);
			} else {
				GameObject.removeObject(gates[0]);
				GameObject.removeObject(gates[1]);
				GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[0]), gates[0].getType(),
						gates[0].getRotation(open ? -1 : 1),
						open ? gates[0].transform(0, -1, 0) : gates[0].transform(0, 1, 0)));
				GameObject.spawnObject(new GameObject(DoorPair.getOpposingDoor(player, gates[1]), gates[1].getType(),
						gates[1].getRotation(open ? -1 : 1),
						open ? gates[1].transform(1, -2, 0) : gates[1].transform(-1, 2, 0)));
			}
			break;
		}
	}

	private static GameObject[] getNearby(Player player, GameObject object,
			BiFunction<WorldTile, WorldTile, Boolean> sort, WorldTile... toCheck) {
		GameObject[] g = new GameObject[2];
		for (WorldTile t : toCheck) {
			if (g[0] == null || g[0].getDefinitions().secondInt == 0
					|| !g[0].getDefinitions().getName().equals(object.getDefinitions().getName())) {
				g[0] = GameObject.getObjectWithId(t, object.getType());
			}
		}
		if (g[0] == null || g[0].getDefinitions().secondInt == 0
				|| !g[0].getDefinitions().getName().equals(object.getDefinitions().getName())) {
			return null;
		}
		if (sort.apply(g[0], object)) {
			g[1] = g[0];
			g[0] = object;
		} else {
			g[1] = object;
		}
		return g;
	}

	private static class Door extends GameObject {
		private GameObject original;

		public Door(int id, int type, int rotation, WorldTile location, GameObject original) {
			super(id, type, rotation, location);
			this.original = new GameObject(original);
		}

		public WorldTile getOriginal() {
			return original;
		}
	}
}

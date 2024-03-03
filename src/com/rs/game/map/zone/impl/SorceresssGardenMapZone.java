package com.rs.game.map.zone.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.constants.NPCNames;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.Mood;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.map.zone.MapZone;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.FadingScreen;
import com.rs.utilities.RandomUtility;

import skills.Skills;
import skills.herblore.Herb.GrimyHerb;
import skills.magic.Magic;
import skills.thieving.impl.SqirkFruitSqueeze.SqirkFruit;

public class SorceresssGardenMapZone extends MapZone {

	public SorceresssGardenMapZone() {
		super("Sorceresss Garden MapZone", MapZoneSafetyCondition.SAFE, MapZoneType.NORMAL);
	}

	@Override
	public void start(Player player) {
		
	}

	@Override
	public void finish(Player player) {
		
	}
	
	@Override
	public boolean canMove(Player player, int dir) {
		return true;
	}
	
	
    private static final WorldTile MIDDLE = new WorldTile(2916, 5475, 0);

    public static void teleportToSorceressGardenNPC(NPC npc, final Player player) {
//       npc.setNextForceTalk(new ForceTalk("Senventior Disthinte Molesko!"));
       teleportToSocreressGarden(player, false);
    }
    
    public static void teleportToSocreressGarden(final Player player, boolean broomstick) {
        if (player.getMapZoneManager().isValidInstance(SorceresssGardenMapZone.class)) {
            player.getPackets().sendGameMessage(
                    "You can't teleport to the Sorceress's Garden whilst you're in the Sorceress's Garden!");
            return;
        }
        if (!broomstick)
            Magic.sendNormalTeleportSpell(player, 0, 0, MIDDLE);
        else
            Magic.sendTeleportSpell(player, 10538, 10537, -1, -1, 0, 0, MIDDLE, 4, true, Magic.MAGIC_TELEPORT);
        player.task(4, thief -> thief.toPlayer().getMapZoneManager().submitMapZone(new SorceresssGardenMapZone()));
    }
    

    /**
     * Checks if the player is in any garden
     */
    public static boolean inGarden(WorldTile tile) {
        return ((tile.getX() >= 2880 && tile.getX() <= 2943) && (tile.getY() >= 5440 && tile.getY() <= 5503));
    }

    public static boolean inGardens(WorldTile tile) {
        return inWinterGarden(tile) || inAutumnGarden(tile) || inSpringGarden(tile) || inSummerGarden(tile);
    }

    /**
     * Checks if the player is at Winter Garden or not
     */
    public static boolean inWinterGarden(WorldTile tile) {
        return ((tile.getX() >= 2886 && tile.getX() <= 2902) && (tile.getY() >= 5464 && tile.getY() <= 5487));
    }

    /**
     * Checks if the player is at Spring Garden or not
     */
    public static boolean inSummerGarden(WorldTile tile) {
        return ((tile.getX() >= 2904 && tile.getX() <= 2927) && (tile.getY() >= 5481 && tile.getY() <= 5497));
    }

    /**
     * Checks if the player is at Summer Garden or not
     */
    public static boolean inSpringGarden(WorldTile tile) {
        return ((tile.getX() >= 2921 && tile.getX() <= 2937) && (tile.getY() >= 5456 && tile.getY() <= 5479));
    }

    /**
     * Checks if the player is at Autumn Garden or not
     */
    public static boolean inAutumnGarden(WorldTile tile) {
        return ((tile.getX() >= 2896 && tile.getX() <= 2919) && (tile.getY() >= 5446 && tile.getY() <= 5462));
    }

    @Override
    public void magicTeleported(Player player, int type) {
    	endMapZoneSession(player);
    }

    @Override
    public boolean login(Player player) {
        return false;
    }

    @Override
    public boolean logout(Player player) {
        return false;
    }

    @Override
    public boolean sendDeath(Player player) {
    	endMapZoneSession(player);
        return true;
    }
    
    @Override
	public boolean processNPCClick1(Player player, NPC npc) {
		if (npc.getId() == 5563)
			player.dialogue(new DialogueEventListener(player, NPCNames.DEL_MONTY_5563) {
				@Override
				public void start() {
					player(Mood.afraid, "Hey kitty!");
					npc(Mood.annoyed, "Hiss!");
				}
			});
		return false;
	}

    @Override
    public boolean processObjectClick1(Player player, GameObject object) {
    	player.faceObject(object);
        if (object.getId() == 21764) {
            player.getMovement().lock();
            player.setNextAnimation(Animations.SG_DRINK_FROM_FOUNTAIN);
            player.task(3, thief -> {
            	thief.toPlayer().getMapZoneManager().endMapZoneSession(thief.toPlayer());
            	thief.toPlayer().getMovement().unlock();
				Magic.sendNormalTeleportSpell(thief.toPlayer(), 0, 0, new WorldTile(3321, 3141, 0));
            });
            return false;
        }
        if (!player.getInventory().hasFreeSlots()){
            player.getPackets().sendGameMessage("You need to free up some inventory space before picking this tree.");
            return false;
        }
        if (RandomUtility.percentageChance(5) && !player.ownsItems(new Item(14057))) {
            player.getInventory().addItem(14057, 1);
        }
        if (object.getId() == 21768) {
            player.setNextAnimation(Animations.TREE_PICKING);
            player.getInventory().addItem(SqirkFruit.AUTUMM.getFruitId(), 1);
            player.getSkills().addExperience(Skills.FARMING, 50);
            teleMiddle(player);
            return false;
        } else if (object.getId() == 21769) {
            player.setNextAnimation(Animations.TREE_PICKING);
            player.getInventory().addItem(SqirkFruit.WINTER.getFruitId(), 1);
            player.getSkills().addExperience(Skills.FARMING, 30);
            teleMiddle(player);
            return false;
        } else if (object.getId() == 21766) {
            player.setNextAnimation(Animations.TREE_PICKING);
            player.getInventory().addItem(SqirkFruit.SUMMER.getFruitId(), 1);
            player.getSkills().addExperience(Skills.FARMING, 60);
            teleMiddle(player);
            return false;
        } else if (object.getId() == 21767) {
            player.setNextAnimation(Animations.TREE_PICKING);
            player.getInventory().addItem(SqirkFruit.SPRING.getFruitId(), 1);
            player.getSkills().addExperience(Skills.FARMING, 40);
            teleMiddle(player);
            return false;
        } else if (object.getDefinitions().getName().toLowerCase().contains("gate")) {
            final Gate gate = Gate.forId(object.getId());
            if (gate != null) {
                Gate.handleGates(player, gate.getObjectId(), gate.getLeveLReq(),
                        inGardens(player) ? gate.getOutsideTile() : gate.getInsideTile(), gate.musicId);
                return false;
            }
        } else if (object.getDefinitions().getName().toLowerCase().equals("herbs")) {
            player.setNextAnimation(Animations.TOUCH_GROUND);
            GrimyHerb.values();
            Optional<GrimyHerb> herbs = GrimyHerb.VALUES.stream().findFirst();
            player.getInventory().addItem(new Item(RandomUtility.random(herbs.get().grimy.getId())));
            player.getInventory().addItem(new Item(RandomUtility.random(herbs.get().grimy.getId())));
            player.getSkills().addExperience(Skills.FARMING,
                    inAutumnGarden(player) ? 50
                            : (inSpringGarden(player) ? 40
                            : (inSummerGarden(player) ? 60 : 30)));
            teleMiddle(player);
            return false;
        }
        return true;
    }

    public void teleMiddle(Player player) {
        player.getMovement().lock();
        player.getPackets().sendGameMessage("An elemental force emanating from the garden teleports you away.");
        player.getDetails().getStatistics().addStatistic("SorceressGarden_Completions");
        FadingScreen.fade(player, new Runnable() {
            @Override
            public void run() {
                player.setNextWorldTile(new WorldTile(2913, 5467, 0));
                player.getMovement().lock(3);
            }

        });
    }

    public enum Gate {

        WINTER(21709, 1, new WorldTile(2902, 5470, 0), new WorldTile(2903, 5470, 0), 231),
        SPRING(21753, 25, new WorldTile(2921, 5473, 0), new WorldTile(2920, 5473, 0), 228),
        AUTUMN(21731, 45, new WorldTile(2913, 5462, 0), new WorldTile(2913, 5463, 0), 229),
        SUMMER(21687, 65, new WorldTile(2910, 5481, 0), new WorldTile(2910, 5480, 0), 230);

        private static Map<Integer, Gate> Gates = new HashMap<Integer, Gate>();

        static {
            for (Gate gate : Gate.values()) {
                Gates.put(gate.getObjectId(), gate);
            }
        }

        private int objectId;
        private int levelReq;
        private int musicId;
        private WorldTile inside, outside;

        private Gate(int objectId, int lvlReq, WorldTile inside, WorldTile outside, int musicId) {
            this.objectId = objectId;
            this.levelReq = lvlReq;
            this.inside = inside;
            this.outside = outside;
            this.musicId = musicId;
        }

        /**
         * @param player   the Player
         * @param objectId Object id
         * @param lvlReq   Level required for entrance
         * @param toTile   Where the player will be spawned
         */
        public static void handleGates(Player player, int objectId, int lvlReq, WorldTile toTile, int musicId) {
            if (player.getSkills().getTrueLevel(Skills.THIEVING) < lvlReq) {
            	player.dialogue(d -> d.mes("You need a thieving level at least " + lvlReq + " to pick this gate."));
                return;
            }
            player.setNextWorldTile(toTile);
            player.getMusicsManager().playMusic(musicId);
        }

        public static Gate forId(int id) {
            return Gates.get(id);
        }

        public int getObjectId() {
            return objectId;
        }

        public int getLeveLReq() {
            return levelReq;
        }

        public WorldTile getInsideTile() {
            return inside;
        }

        public WorldTile getOutsideTile() {
            return outside;
        }
    }
}
package skills.prayer;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.SlimeBucketFill;
import com.rs.net.encoders.other.Animation;

import skills.Skills;
import skills.magic.TeleportType;

public class EctoFuntus {

    private static final int HOPPER_OBJECT = 11162;
    private static final int GRINDER_OBJECT = 11163;
    private static final int BIN_OBJECT = 11164;

    public static void handleWorship(Player player) {
        if (player.getInventory().containsItem(4252, 1)) {
            player.setNextAnimation(new Animation(1649));
            player.getInventory().deleteItem(4252, 1);
            player.getInventory().addItem(4251, 1);
            player.getPackets().sendGameMessage("You refill the ectophial from the Ectofuntus.");
            return;
        }
        if (!player.getInventory().contains(new Item(4286))) {
            player.getPackets().sendGameMessage("You need a bucket of slime before you can worship the ectofuntus.");
            return;
        }
        for (Item item : player.getInventory().getItems().getItems()) {
            if (item == null)
                continue;
            BoneMeal bone = BoneMeal.forMealId(item.getId());
            if (bone != null) {
            	Bone.VALUES.stream().filter(boneOf -> boneOf.getId() == bone.getBoneId())
            	.forEach(boneAccepted -> {
            		   player.setNextAnimation(new Animation(1651));
                       player.getInventory().deleteItem(bone.getBoneMealId(), 1);
                       player.getInventory().addItem(1931, 1);
                       player.getInventory().deleteItem(4286, 1);
                       player.getInventory().addItem(1925, 1);
                       player.getSkills().addExperience(Skills.PRAYER, boneAccepted.getExperience() * 4);
                       player.getDetails().getStatistics().addStatistic("Ectofuntus_Worships");
            	});
                return;
            }
        }
    }

    public static boolean handleObjects(final Player player, final GameObject object) {
    	int objectId = object.getId();
        switch (objectId) {
        case 5281:
        	player.setNextWorldTile(new WorldTile(player.getX(), player.getY() - 6, (player.getPlane() == 0 ? 1 : 0)));
    		return true;
        	case 37454:
        		player.setNextWorldTile(new WorldTile(player.getX(), player.getY() + 5, (player.getPlane() == 0 ? 1 : 0)));
        		return true;
            case 11162:
                player.getPackets().sendGameMessage("You must place a Bone inside the hopper to operate this properly.");
                return true;
            case 5278:
                player.getPackets().sendGameMessage("Nothing special is added for this coffin. Do what you'd like!");
                return true;
            case 5267:
                player.getMovement().move(false, new WorldTile(3669, 9888, 3), TeleportType.LADDER);
                return true;

            case 5264:
            	player.getMovement().move(false, new WorldTile(3654, 3519, 0), TeleportType.LADDER);
                return true;

            case 9308:
                if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
                    player.getPackets().sendGameMessage("You need 53 Agility to maneuver this obstacle.");
                    return true;
                }
                player.getMovement().move(false, new WorldTile(3671, 9888, 2), TeleportType.LADDER);
                return true;

            case 9307:
                if (player.getSkills().getLevel(Skills.AGILITY) < 53) {
                    player.getPackets().sendGameMessage("You need 53 Agility to maneuver this obstacle.");
                    return true;
                }
                player.getMovement().move(false, new WorldTile(new WorldTile(3670, 9888, 3)), TeleportType.LADDER);
                return true;

            case 5263:
                if (player.getPlane() == 3)
                    player.setNextWorldTile(new WorldTile(3688, 9888, 2));
                if (player.getPlane() == 2)
                    player.setNextWorldTile(new WorldTile(3675, 9887, 1));
                if (player.getPlane() == 1)
                    player.setNextWorldTile(new WorldTile(3683, 9888, 0));
                return true;

            case 5262:
                if (player.getPlane() == 2)
                    player.setNextWorldTile(new WorldTile(3692, 9888, 3));
                if (player.getPlane() == 1)
                    player.setNextWorldTile(new WorldTile(3671, 9888, 2));
                if (player.getPlane() == 0)
                    player.setNextWorldTile(new WorldTile(3687, 9888, 1));
                return true;

            case 5282:
            	player.faceObject(object);
                handleWorship(player);
                return true;

            case GRINDER_OBJECT:
                if (player.getDetails().getBoneType().get() != -1) {
                    player.getPackets().sendGameMessage("You turn the grinder, some crushed bones fall into the bin.");
                    player.setNextAnimation(new Animation(1648));
                    player.getDetails().getBonesGrinded().setValue(true);
                    return true;
                } else {
                    player.getPackets().sendGameMessage("You have nothing to grind.");
                }
                return true;

            case BIN_OBJECT:
                if (player.getDetails().getBoneType().get() == -1) {
                    player.getPackets().sendGameMessage("You need to put some bones in the hopper and grind them first.");
                    return true;
                }
                if (player.getDetails().getBonesGrinded().isFalse()) {
                    player.getPackets().sendGameMessage("You need to grind the bones by turning the grinder first.");
                    return true;
                }
                if (!player.getInventory().containsItem(1931, 1)) {
                    player.getPackets().sendGameMessage("You need an empty pot to fill with the crushed bones.");
                    return true;
                }
                if (player.getDetails().getBoneType().get() != -1 && player.getDetails().getBonesGrinded().isTrue()) {
                    BoneMeal meal = BoneMeal.forBoneId(player.getDetails().getBoneType().get());
                    if (meal != null) {
                        player.getPackets().sendGameMessage("You fill an empty pot with bones.");
                        player.setNextAnimation(new Animation(1650));
                        player.getInventory().deleteItem(1931, 1);
                        player.getInventory().addItem(meal.getBoneMealId(), 1);
                        player.getDetails().getBoneType().set(-1);
                        player.getDetails().getBonesGrinded().setValue(false);
                    } else {
                        player.getDetails().getBoneType().set(-1);
                    }
                }
                return true;
        }
        return false;
    }

    public static boolean handleItemOnObject(Player player, int itemId, int objectId) {
        ItemDefinitions itemDefs = ItemDefinitions.getItemDefinitions(itemId);

        if (itemId == 1925 && objectId == 17119) {
            player.getAction().setAction(new SlimeBucketFill());
            return true;
        }

        if (itemDefs.getName().toLowerCase().contains("bone") && objectId == HOPPER_OBJECT) {
            if (player.getDetails().getBoneType().get() != -1 && player.getDetails().getBoneType().get() != 0) {
                player.getPackets().sendGameMessage("You already have some bones in the hopper.");
                return true;
            }
            BoneMeal meal = BoneMeal.forBoneId(itemId);
            if (meal != null) {
            	player.getDetails().getBoneType().set(meal.getBoneId());
                player.getPackets().sendGameMessage("You put the bones in the hopper.");
                player.setNextAnimation(new Animation(1649));
                player.getInventory().deleteItem(meal.getBoneId(), 1);
                player.getDetails().getBoneType().set(meal.boneId);
                return false;
            } else {
            	player.getDetails().getBoneType().set(-1);
            }
            return false;
        }
        return false;
    }

    private enum BoneMeal {
        BONES(ItemNames.BONES_526, 4255),
        BAT_BONES(ItemNames.BAT_BONES_530, 4256),
        BIG_BONES(ItemNames.BIG_BONES_532, 4257),
        BABY_DRAGON_BONES(534, 4260),
        DRAGON_BONES(ItemNames.DRAGON_BONES_536, 4261),
        DAGANNOTH_BONES(ItemNames.DAGANNOTH_BONES_6729, 6728),
        WYVERN_BONES(ItemNames.WYVERN_BONES_6812, 6810),
        OURG_BONES(ItemNames.OURG_BONES_4834, 4855),
        FROST_BONES(ItemNames.FROST_DRAGON_BONES_18830, 18834);

        private static Map<Integer, BoneMeal> bonemeals = new HashMap<>();
        private static Map<Integer, BoneMeal> bones = new HashMap<>();

        static {
            for (final BoneMeal bonemeal : BoneMeal.values()) {
                bonemeals.put(bonemeal.boneId, bonemeal);
            }
            for (final BoneMeal bonemeal : BoneMeal.values()) {
                bones.put(bonemeal.boneMealId, bonemeal);
            }
        }

        private int boneId;
        private int boneMealId;

        BoneMeal(int boneId, int boneMealId) {
            this.boneId = boneId;
            this.boneMealId = boneMealId;
        }

        public static BoneMeal forBoneId(int itemId) {
            return bonemeals.get(itemId);
        }

        public static BoneMeal forMealId(int itemId) {
            return bones.get(itemId);
        }

        public int getBoneId() {
            return boneId;
        }

        public int getBoneMealId() {
            return boneMealId;
        }
    }
}
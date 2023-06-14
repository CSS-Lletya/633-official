package skills.woodcutting.sawmill;

import com.rs.constants.Sounds;
import com.rs.content.mapzone.impl.SawmillMapZone;
import com.rs.game.Entity;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.utilities.IntegerInputAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import skills.Skills;

public class Sawmill {

    public static void openPlanksConverter(Player player) {
        player.getInterfaceManager().sendInterface(403);
    }

    public static void handlePlanksConvertButtons(Player player, int componentId, int packetId) {
        if (componentId >= 12 && componentId <= 15) {
            Plank type = Plank.values()[componentId - 12];
            if (packetId == 9) {
            	player.getPackets().sendInputIntegerScript("How many would you like to convert?", new IntegerInputAction() {
					@Override
					public void handle(int input) {
						convertPlanks(player, type, input);
					}
				});
            	return;
            }
			int amount = packetId == 1 ? 1
					: packetId == 2 ? 5 : packetId == 3 ? 10 : player.getInventory().getNumberOf(type.logId);
			convertPlanks(player, type, amount);
		}
	}

    public static void convertPlanks(Player player, Plank type, int amount) {
        int logsAmt = player.getInventory().getNumberOf(type.getLogId());
        int cost = amount * type.getCost();
        if (logsAmt == 0) {
            player.getPackets().sendGameMessage("You don't have any logs.");
            return;
        }
        if (amount > logsAmt) {
            amount = logsAmt;
            cost = amount * type.getCost();
            player.getPackets().sendGameMessage("You don't have that many logs.");
        }
        if (cost > player.getInventory().getNumberOf(995)) {
            amount = player.getInventory().getNumberOf(995) / type.cost;
            cost = amount * type.getCost();
            if (amount < 1) {
                player.getPackets().sendGameMessage("You don't have enough coins to do this.");
                return;
            }
        }
        if (player.getInventory().containsItem(995, cost)) {
            player.getInterfaceManager().closeInterfaces();
            player.getInventory().deleteItem(new Item(995, cost));
            player.getInventory().deleteItem(type.getLogId(), amount);
            player.getInventory().addItem(type.getId(), amount);
            player.getAudioManager().sendSound(Sounds.SAWMILL_PLANK_CONVERT);
        } else {
            player.getPackets().sendGameMessage("You don't have enough coins to do this.");
        }
    }

    @AllArgsConstructor
    public static enum Plank {
        WOOD(960, 1511, 100), OAK(8778, 1521, 250),
        TEAK(8780, 6333, 500), MAHOGANY(8782, 6332, 1500);
    	
    	@Getter
        private int id, logId, cost;
    }
    
    public static Plank getPlankForLog(int id) {
        for (Plank plank : Plank.values())
            if (plank.logId == id)
                return plank;
        return null;
    }

    public static Plank getPlank(int id) {
        for (Plank plank : Plank.values())
            if (plank.id == id)
                return plank;
        return null;
    }

    public static boolean hasPlanksOrLogs(Player player) {
        for (Item item : player.getInventory().getItems().getItems()) {
            if (item != null && (getPlankForLog(item.getId()) != null || getPlank(item.getId()) != null))
                return true;
        }
        return false;
    }

    public static void enter(Player player, GameObject object) {
        if (player.getSkills().getLevel(Skills.WOODCUTTING) < 80) {
        	player.dialog(new DialogueEventListener(player, Entity.findNPC(SawmillMapZone.OVERSEER)) {
				@Override
				public void start() {
					npc(sad,"Sorry, we don't need inexperienced woodcutters.");
				}
			});
            return;
        }
        if (player.getInventory().containsAny((946))) {
        	player.dialog(new DialogueEventListener(player, Entity.findNPC(SawmillMapZone.OVERSEER)) {
				@Override
				public void start() {
					npc(sad,"Sorry, but we don't allow any fletching knives to be brought in here.");
				}
			});
            return;
        }
        if (hasPlanksOrLogs(player)) {
        	player.dialog(new DialogueEventListener(player, Entity.findNPC(SawmillMapZone.OVERSEER)) {
				@Override
				public void start() {
					npc(sad,"Sorry, you can't bring any planks or logs in with you. You might get them muddled with ours.");
				}
			});
            return;
        }
        player.getMovement().lock(2);
        player.addWalkSteps(object.getX() + 1, object.getY(), 1, false);
        player.getMapZoneManager().submitMapZone(player, new SawmillMapZone());
    }
}
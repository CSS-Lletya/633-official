package skills.woodcutting;

import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.IntegerInputAction;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
}
package com.rs.game.player;

import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.item.ItemsContainer;
import com.rs.utilities.ItemExamines;
import com.rs.utilities.Utility;

public class Trade {

    private transient Player player, target;
    private ItemsContainer<Item> items;
    private boolean tradeModified;
    private boolean accepted;

    public Trade(Player player) {
        this.player = player; //player reference
        items = new ItemsContainer<Item>(28, false);
    }

    /*
     * called to both players
     */
    public void openTrade(Player target) {
        synchronized (this) {
            synchronized (target.getTrade()) {
                this.target = target;
                player.getPackets().sendIComponentText(335, 15, "Trading With: " + target.getDisplayName());
                player.getPackets().sendHideIComponent(335, 43, true);
                player.getPackets().sendHideIComponent(335, 45, true);
                player.getPackets().sendGlobalString(203, target.getDisplayName());
                sendInterItems();
                sendOptions();
                sendTradeModified();
                refreshFreeInventorySlots();
                refreshTradeWealth();
                refreshStageMessage(true);
                player.getInterfaceManager().sendInterface(335);
                player.getInterfaceManager().sendInventoryInterface(336);
                player.setCloseInterfacesEvent(new Runnable() {
                    @Override
                    public void run() {
                        closeTrade(CloseTradeStage.CANCEL);
                    }
                });
            }
        }
    }


    public void removeItem(final int slot, int amount) {
        synchronized (this) {
            if (!isTrading())
                return;
            synchronized (target.getTrade()) {
                Item item = items.get(slot);
                if (item == null)
                    return;
                Item[] itemsBefore = items.getItemsCopy();
                int maxAmount = items.getNumberOf(item);
                if (amount < maxAmount)
                    item = new Item(item.getId(), amount);
                else
                    item = new Item(item.getId(), maxAmount);
                items.remove(slot, item);
                player.getInventory().addItem(item);
                refreshItems(itemsBefore);
                cancelAccepted();
                setTradeModified(true);
            }
        }
    }

    public void sendFlash(int slot) {
    	player.getPackets().sendInterFlashScript(335, 32, 4, 7, slot);
        target.getPackets().sendInterFlashScript(335, 33, 4, 7, slot);
    }

    public void cancelAccepted() {
        boolean canceled = false;
        if (accepted) {
            accepted = false;
            canceled = true;
        }
        if (target.getTrade().accepted) {
            target.getTrade().accepted = false;
            canceled = true;
        }
        if (canceled)
            refreshBothStageMessage(canceled);
    }

    public void addItem(int slot, int amount) {
        synchronized (this) {
            if (!isTrading())
                return;
            synchronized (target.getTrade()) {
                Item item = player.getInventory().getItem(slot);
                if (item == null)
                    return;
                if (!ItemConstants.isTradeable(item)) {
                    player.getPackets().sendGameMessage("That item isn't tradeable.");
                    return;
                }
                Item[] itemsBefore = items.getItemsCopy();
                int maxAmount = player.getInventory().getItems().getNumberOf(item);
                if (amount < maxAmount)
                    item = new Item(item.getId(), amount);
                else
                    item = new Item(item.getId(), maxAmount);
                items.add(item);
                player.getInventory().deleteItem(slot, item);
                refreshItems(itemsBefore);
                cancelAccepted();
            }
        }
    }

    public void refreshItems(Item[] itemsBefore) {
        int[] changedSlots = new int[itemsBefore.length];
        int count = 0;
        for (int index = 0; index < itemsBefore.length; index++) {
            Item item = items.getItems()[index];
            if (itemsBefore[index] != item) {
                if (itemsBefore[index] != null && (item == null || item.getId() != itemsBefore[index].getId() || item.getAmount() < itemsBefore[index].getAmount()))
                    sendFlash(index);
                changedSlots[count++] = index;
            }
        }
        int[] finalChangedSlots = new int[count];
        System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
        refresh(finalChangedSlots);
        refreshFreeInventorySlots();
        refreshTradeWealth();
    }

    public void sendOptions() {
        player.getPackets().sendInterSetItemsOptionsScript(336, 0, 93, 4, 7,
                "Offer", "Offer-5", "Offer-10", "Offer-All", "Offer-X");
        player.getPackets().sendIComponentSettings(336, 0, 0, 27, 1278);
        player.getPackets().sendInterSetItemsOptionsScript(335, 32, 90, 4, 7,
                "Remove", "Remove-5", "Remove-10", "Remove-All", "Remove-X");
        player.getPackets().sendIComponentSettings(335, 32, 0, 27, 1150);
        player.getPackets().sendInterSetItemsOptionsScript(335, 35, 90, true, 4, 7);
        player.getPackets().sendIComponentSettings(335, 35, 0, 27, 1026);

    }

    public boolean isTrading() {
        return target != null;
    }

    public void setTradeModified(boolean modified) {
        if (modified == tradeModified)
            return;
        tradeModified = modified;
        sendTradeModified();
    }

    public void sendInterItems() {
        player.getPackets().sendItems(90, items);
        target.getPackets().sendItems(90, true, items);
    }

    public void refresh(int... slots) {
        player.getPackets().sendUpdateItems(90, items, slots);
        target.getPackets().sendUpdateItems(90, true, items.getItems(), slots);
    }

	public void accept(boolean firstStage) {
		synchronized (this) {
			synchronized (target.getTrade()) {
				if (target.getTrade().accepted) {
					for (Item item : target.getTrade().items.getItems()) {
						if (item == null)
							continue;
						if (item.getId() != 995) {
							if (player.getInventory().getNumberOf(item.getId()) + item.getAmount() < 0) {
								player.setCloseInterfacesEvent(null);
								player.getInterfaceManager().closeInterfaces();
								closeTrade(CloseTradeStage.NO_SPACE);
								break;
							}
							continue;
						}
						if (player.getInventory().getNumberOf(item.getId()) + item.getAmount() < 0) {
							if (item.getId() == 995) {
								if (player.getInventory().getNumberOf(995) + item.getAmount() < 0) {
									player.setCloseInterfacesEvent(null);
									player.getInterfaceManager().closeInterfaces();
									closeTrade(CloseTradeStage.NO_SPACE);
									break;
								}
							}
							continue;
						}
					}
					if (firstStage) {
						if (nextStage())
							target.getTrade().nextStage();
					} else {
						player.setCloseInterfacesEvent(null);
						player.getInterfaceManager().closeInterfaces();
						closeTrade(CloseTradeStage.DONE);
					}
					return;
				}
				accepted = true;
				refreshBothStageMessage(firstStage);
			}
		}
	}

    public void sendValue(int slot, boolean traders) {
        if (!isTrading())
            return;
        Item item = traders ? target.getTrade().items.get(slot) : items.get(slot);
        if (item == null)
            return;
        if (!ItemConstants.isTradeable(item)) {
            player.getPackets().sendGameMessage("That item isn't tradeable.");
            return;
        }
        player.getPackets().sendGameMessage(ItemExamines.getExamine(item) + ". Value: " + item.getDefinitions().getFormatedItemValue() + "gp.");
    }

    public void sendValue(int slot) {
        Item item = player.getInventory().getItem(slot);
        if (item == null)
            return;
        if (!ItemConstants.isTradeable(item)) {
            player.getPackets().sendGameMessage("That item isn't tradeable.");
            return;
        }
        player.getPackets().sendGameMessage(ItemExamines.getExamine(item) + ". Value: " + item.getDefinitions().getFormatedItemValue() + "gp.");
    }

    public void sendExamine(int slot, boolean traders) {
        if (!isTrading())
            return;
        Item item = traders ? target.getTrade().items.get(slot) : items.get(slot);
        if (item == null)
            return;
        player.getPackets().sendGameMessage(ItemExamines.getExamine(item) + ". Value: " + item.getDefinitions().getFormatedItemValue() + "gp.");
    }

    public boolean nextStage() {
    	if (target == null || player == null) {
			player.setCloseInterfacesEvent(null);
			player.getInterfaceManager().closeInterfaces();
			target.setCloseInterfacesEvent(null);
			target.getInterfaceManager().closeInterfaces();
			closeTrade(CloseTradeStage.CANCEL);
			return false;
		}
		if (player.getInventory().getItems().getUsedSlots() + target.getTrade().items.getUsedSlots() > 28
				|| player.getInventory().getItems().goesOverAmount(items)) {
			player.setCloseInterfacesEvent(null);
			player.getInterfaceManager().closeInterfaces();
			closeTrade(CloseTradeStage.NO_SPACE);
			return false;
		}
		accepted = false;
		player.getInterfaceManager().sendInterface(334);
        player.getInterfaceManager().closeInventory();
//        player.getPackets().sendHideIComponent(334, 55, !(tradeModified || target.getTrade().tradeModified));
        refreshBothStageMessage(false);
        return true;
    }

    public void refreshBothStageMessage(boolean firstStage) {
        refreshStageMessage(firstStage);
        target.getTrade().refreshStageMessage(firstStage);
    }

    public void refreshStageMessage(boolean firstStage) {
        player.getPackets().sendIComponentText(firstStage ? 335 : 334, firstStage ? 38 : 33, getAcceptMessage(firstStage));
    }

    public String getAcceptMessage(boolean firstStage) {
        if (accepted)
            return "Waiting for other player...";
        if (target.getTrade().accepted)
            return "Other player has accepted.";
        return firstStage ? "" : "Are you sure you want to make this trade?";
    }

    public void sendTradeModified() {
        player.getVarsManager().sendVar(1042, tradeModified ? 1 : 0);
        target.getVarsManager().sendVar(1043, tradeModified ? 1 : 0);
    }

    public void refreshTradeWealth() {
        String wealth = Utility.getFormattedNumber(getTradeWealth());
        player.getPackets().sendIComponentText(335, 44, "Trade Value: " + wealth + "gp");
        target.getPackets().sendIComponentText(335, 44, "Trade Value: " + wealth + "gp");
        
    }

    public void refreshFreeInventorySlots() {
        int freeSlots = player.getInventory().getFreeSlots();
        target.getPackets().sendIComponentText(335, 21, "has " + (freeSlots == 0 ? "no" : freeSlots) + " free"
                + "<br>inventory slots");
    }

    public int getTradeWealth() {
        int wealth = 0;
        for (Item item : items.getItems()) {
            if (item == null)
                continue;
            wealth += item.getDefinitions().getValue() * item.getAmount();
        }
        return wealth;
    }

    public void closeTrade(CloseTradeStage stage) {
        synchronized (this) {
            synchronized (target.getTrade()) {
                Player oldTarget = target;
                target = null;
                tradeModified = false;
                accepted = false;
                if (oldTarget.getTrade().isTrading()) {
                    oldTarget.setCloseInterfacesEvent(null);
                    oldTarget.getInterfaceManager().closeInterfaces();
                    oldTarget.getTrade().closeTrade(stage);
                    if (CloseTradeStage.CANCEL == stage) {
                        player.getPackets().sendGameMessage("You have declined the trade!");
                        oldTarget.getPackets().sendGameMessage("<col=ff0000>" + player.getDisplayName() + " declined trade!");
                    } else if (CloseTradeStage.NO_SPACE == stage) {
                        player.getPackets().sendGameMessage("You don't have enough space in your inventory for this trade.");
                        oldTarget.getPackets().sendGameMessage("Other player doesn't have enough space in their inventory for this trade.");
                    }
                }
                StringBuilder given = new StringBuilder();
                for (Item item : oldTarget.getTrade().items.getItemsCopy()) {
                    if (item == null)
                        continue;
                    given.append(item.getName()).append(", ");
                }
                StringBuilder received = new StringBuilder();
                for (Item item : player.getTrade().items.getItemsCopy()) {
                    if (item == null)
                        continue;
                    received.append(item.getName()).append(", ");
                }
//                PassiveDatabaseWorker.addRequest(new SendTradeSQLPlugin(player.getDisplayName(), oldTarget.getDisplayName(), given.toString(), received.toString()));
                if (CloseTradeStage.DONE != stage) {
                    player.getInventory().getItems().addAll(items);
                    player.getInventory().init();
                    items.clear();
                } else {
                    player.getPackets().sendGameMessage("Accepted trade.");
                    player.getInventory().getItems().addAll(oldTarget.getTrade().items);
                    player.getInventory().init();
                    oldTarget.getTrade().items.clear();

                }
            }
        }
    }

    private static enum CloseTradeStage {
        CANCEL, NO_SPACE, DONE
    }

}

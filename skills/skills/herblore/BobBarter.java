package skills.herblore;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class BobBarter {

    private Player player;
    @SuppressWarnings("rawtypes")
    private CopyOnWriteArrayList<ObjectArrayList> potsList;
    private CopyOnWriteArrayList<Integer> delete;

    public BobBarter(Player player) {
        this.setPlayer(player);
    }

    public void addToDeleteList(int id, int amount) {
        for (int i = 0; i < delete.size(); i += 2) {
            if (delete.get(i) == id) {
                delete.set(i + 1, delete.get(i + 1) + amount);
                return;
            }
        }
        delete.add(id);
        delete.add(amount);
    }

    public boolean checkForPots(String itemName) {
        for (int i = 0; i < potsList.size(); i++) {
            if (potsList.get(i).get(0).toString().substring(0, potsList.get(i).get(0).toString().length() - 4)
                    .equals(itemName.substring(0, itemName.length() - 4))) {
                return false;
            }
        }
        return true;
    }

    public boolean checkIfNoted(String itemName) {
        return itemName.charAt(0) == 'n';
    }

    public void checkMaxPots(int pots) {
        int maxPots = 1000000;
        if (pots > maxPots) {
            player.getPackets().sendGameMessage("You can only decant 1 millions potions at a time");
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void decant() {
        potsList = new CopyOnWriteArrayList<>();
        delete = new CopyOnWriteArrayList<Integer>();
        int totalVials = 0;
        for (int i = 0; i < player.getInventory().getItemsContainerSize(); i++) {
            Item item = player.getInventory().getItem(i);
            String itemName = null;
            if (item == null || item.getName() == null) {
                continue;
            }

            if (item.getName().contains("potion") || !item.getName().contains("potion")) {
                for (int a = 1; a < 5; a++) {
                    for (int f = 110; f < Utility.getItemDefinitionsSize(); f++) {
                        if (f == 278)
                            f = 2426;
                        if (f == 2460)
                            f = 3007;
                        if (f == 3048)
                            f = 5937;
                        if (f == 5960)
                            f = 6469;
                        if (f == 6478)
                            f = 6684;
                        if (f == 6693)
                            f = 9738;
                        if (f == 9747)
                            f = 9997;
                        if (f == 10006)
                            f = 12139;
                        if (f == 12148)
                            f = 15308;
                        if (f == 15335)
                            f = 21629;

                        if (item.getName().contains("(" + a + ")")
                                && item.getName().equals(ItemDefinitions.getItemDefinitions(f).getName())) {
                            if (!item.getDefinitions().isNoted()) {
                                itemName = item.getName();
                                if (checkForPots(itemName)) {
                                    potsList.add(new ObjectArrayList());
                                    potsList.get(potsList.size() - 1).add(itemName);
                                } else {
                                    potsList.get(potPlace(itemName)).add(itemName);
                                }
                                totalVials += item.getAmount();
                                checkMaxPots(totalVials);
                                addToDeleteList(item.getId(), item.getAmount());
                            } else {
                                itemName = "n" + item.getName();
                                if (checkForPots(itemName)) {
                                    potsList.add(new ObjectArrayList());
                                    for (int q = 0; q < item.getAmount(); q++) {
                                        potsList.get(potsList.size() - 1).add(itemName);
                                    }
                                } else {
                                    for (int q = 0; q < item.getAmount(); q++) {
                                        potsList.get(potPlace(itemName)).add(itemName);
                                    }
                                }
                                totalVials += item.getAmount();
                                checkMaxPots(totalVials);
                                addToDeleteList(item.getId(), item.getAmount());
                            }
                            break;
                        }
                    }
                }
            }
        }
        checkMaxPots(totalVials);
        for (int i = 0; i < delete.size(); i += 2) {
            player.getInventory().deleteItem(delete.get(i), delete.get(i + 1));
        }

        for (ObjectArrayList arrayList : potsList) {

            int vials = 0, totalDoses = 0, partDoses, fullVialId = 0, partVialId = 0, fullVials, partVials = 0,
                    emptyVialId = 0;
            String itemName = null, fullVial = null, partVial = null;
            for (int a = 0; a < arrayList.size(); a++) {
                itemName = arrayList.get(a).toString();
                vials++;
                totalDoses += Integer.parseInt(itemName.substring(itemName.length() - 2, itemName.length() - 1));
            }

            assert itemName != null;
            if (itemName.contains("potion") || !itemName.contains("potion")) {

                fullVials = totalDoses / 4;
                partDoses = totalDoses % 4;
                for (int x = 1; x < 5; x++) {
                    if (itemName.contains("(" + x + ")")) {
                        if (!checkIfNoted(itemName)) {
                            emptyVialId = 229;
                            fullVial = itemName.replace(Integer.toString(x), "4");
                            partVial = itemName.replace(Integer.toString(x), Integer.toString(partDoses));
                        } else {
                            emptyVialId = 230;
                            fullVial = itemName.substring(1, itemName.length()).replace(Integer.toString(x), "4");
                            partVial = itemName.substring(1, itemName.length()).replace(Integer.toString(x),
                                    Integer.toString(partDoses));
                        }
                    }
                }
                for (int f = 110; f < Utility.getItemDefinitionsSize(); f++) {
                    if (f == 278)
                        f = 2426;
                    if (f == 2460)
                        f = 3007;
                    if (f == 3048)
                        f = 5937;
                    if (f == 5960)
                        f = 6469;
                    if (f == 6478)
                        f = 6684;
                    if (f == 6693)
                        f = 9738;
                    if (f == 9747)
                        f = 9997;
                    if (f == 10006)
                        f = 12139;
                    if (f == 12148)
                        f = 15308;
                    String name = ItemDefinitions.getItemDefinitions(f).getName();

                    if (!checkIfNoted(itemName)) {
                        if (name.equals(fullVial)) {

                            if (f < 18738) {
                                if (!itemName.contains("Extreme") && !itemName.contains("Overload")) {
                                    if (ItemDefinitions.getItemDefinitions(f).isNoted()) {
                                        fullVialId = f - 1;
                                    } else {
                                        fullVialId = f;
                                    }
                                } else {
                                    if (ItemDefinitions.getItemDefinitions(f).isNoted()) {
                                        fullVialId = f - 11304;
                                    } else {
                                        fullVialId = f;
                                    }
                                }

                            }

                        } else if (name.equals(partVial)) {
                            if (ItemDefinitions.getItemDefinitions(f).isNoted()) {
                                partVialId = f - 1;
                            } else {
                                partVialId = f;
                            }
                        }
                    } else {
                        if (name.equals(fullVial)) {

                            if (ItemDefinitions.getItemDefinitions(f).isNoted()) {
                                fullVialId = f;
                            }
                        } else if (name.equals(partVial)) {
                            if (ItemDefinitions.getItemDefinitions(f).isNoted()) {
                                partVialId = f;
                            }
                        }
                    }

                }

                if (fullVials > 0) {
                    player.getInventory().addItem(new Item(fullVialId, fullVials));
                }
                if (partDoses != 0) {
                    partVials = 1;
                    player.getInventory().addItem(new Item(partVialId, 1));
                }
                if (vials - (fullVials + partVials) > 0) {
                    player.getInventory().addItem(new Item(emptyVialId, vials - (fullVials + partVials)));
                }
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int potPlace(String itemName) {
        for (int i = 0; i < potsList.size(); i++) {
            if (potsList.get(i).get(0).toString().substring(0, potsList.get(i).get(0).toString().length() - 4)
                    .equals(itemName.substring(0, itemName.length() - 4))) {
                return i;
            }
        }
        return -1;
    }
}
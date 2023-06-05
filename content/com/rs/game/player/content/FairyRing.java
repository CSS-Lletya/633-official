package com.rs.game.player.content;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;

public class FairyRing {

    private final static String[][] LETTERS = new String[][]
            {
                    {"a", "b", "c", "d"},
                    {"i", "j", "k", "l"},
                    {"p", "q", "r", "s"}};
    private final static WorldTile FAIRY_SOURCE = new WorldTile(2412, 4434, 0);
    private final static int FIRST_ANIMATION = 3254, SECOND_ANIMATION = 3255;
    private final static int FIRST_GRAPHICS = 2670, SECOND_GRAPHICS = 2671;

    public static boolean checkAll(Player player) {
        if (player.getEquipment().getWeaponId() != 772 && player.getEquipment().getWeaponId() != 9084) {
        	player.getPackets().sendGameMessage("The fairy ring only works for those who wield fairy magic.");
        	return false;
        }
        return true;
    }

    public static boolean openRingInterface(Player player, WorldTile tile, boolean source) {
        if (checkAll(player)) {
            player.addWalkSteps(tile.getX(), tile.getY(), -1, true);
            if (source) {
                player.getInterfaceManager().sendInterface(734);
                sendTravelLog(player);
                resetRingHash(player);
            } else
                sendTeleport(player, FAIRY_SOURCE);
            return true;
        }
        return false;
    }

    private static void sendTravelLog(Player player) {
        player.getInterfaceManager().sendInventoryInterface(735);
        player.getPackets().sendIComponentText(735, 15, "          Asgarnia: Mudskipper Point");
        player.getPackets().sendIComponentText(735, 16, "          Islands: South of Witchaven");
        player.getPackets().sendIComponentText(735, 19, "          Dungeons: Dark cave south of Dorgesh-Kaan");
        player.getPackets().sendIComponentText(735, 20, "          Kandarin: Slayer cave south-east of Rellekka");
        player.getPackets().sendIComponentText(735, 21, "          Islands: Penguins near Miscellania");
        player.getPackets().sendIComponentText(735, 23, "          Kandarin: Piscatoris Hunter area");
        player.getPackets().sendIComponentText(735, 25, "          Feldip Hills: Feldip Hunter area");
        player.getPackets().sendIComponentText(735, 26, "          Kandarin: Feldip Hills");
        player.getPackets().sendIComponentText(735, 27, "          Morytania: Haunted Woods east of Canifis");
        player.getPackets().sendIComponentText(735, 28, "          Other Realms: Abyss");
        player.getPackets().sendIComponentText(735, 29, "          Kandarin: McGrubor's Wood");
        player.getPackets().sendIComponentText(735, 31, "          Kharidian Desert: Near Kalphite hive");
        player.getPackets().sendIComponentText(735, 32, "          Sparse Plane");
        player.getPackets().sendIComponentText(735, 33, "          Kandarin: Ardougne Zoo unicorns");
        player.getPackets().sendIComponentText(735, 35, "          Dungeons: Ancient Dungeon");
        player.getPackets().sendIComponentText(735, 36, "          Fisher Realm");
        player.getPackets().sendIComponentText(735, 38, "          Feldip Hills: South of Castle Wars");
        player.getPackets().sendIComponentText(735, 39, "          Other Realms: Enchanted Valley");
        player.getPackets().sendIComponentText(735, 40, "          Morytania: Mort Myre, south of Canifis");
        player.getPackets().sendIComponentText(735, 42, "          Dungeons: TzHaar area");
        player.getPackets().sendIComponentText(735, 44, "          Kandarin: Legend's Guild");
        player.getPackets().sendIComponentText(735, 46, "          North-west Miscellania"); 
        player.getPackets().sendIComponentText(735, 47, "          Kandarin: North-west of Yanille");
        player.getPackets().sendIComponentText(735, 52, "          Kandarin: Sinclair Mansion");
        player.getPackets().sendIComponentText(735, 56, "          Karamja: South of Tai Bwo Wannai Village");
        player.getPackets().sendIComponentText(735, 57, "          Morytania: Canifis");
        player.getPackets().sendIComponentText(735, 60, "          Ape Atoll: West of the Ape Atoll Agility Course");
        player.getPackets().sendIComponentText(735, 61, "          Islands: Jungle spiders near Yanille");
        player.getPackets().sendIComponentText(735, 62, "          Mos Le'Harmless: Isle on the coast of Mos Le'Harmless");
        player.getPackets().sendIComponentText(735, 65, "          Misthalin: Wizards' Tower");
        player.getPackets().sendIComponentText(735, 66, "          Kandarin: Tower of Life");
        player.getPackets().sendIComponentText(735, 68, "          Kandarin: Sinclair Mansion");
        player.getPackets().sendIComponentText(735, 70, "          Karamja: South of Musa Point");
        player.getPackets().sendIComponentText(735, 72, "          Misthalin: Edgeville");
        player.getPackets().sendIComponentText(735, 73, "          Kandarin: Snowy Hunter area");
        player.getPackets().sendIComponentText(735, 75, "          Kharidian Desert: North of Nardah");
        player.getPackets().sendIComponentText(735, 77, "          Dungeons: Myreque hideout under The Hollows");
        player.getPackets().sendIComponentText(735, 54, "          Other Realms: Cosmic Entity's plane");
        player.getPackets().sendIComponentText(735, 58, "          Islands: South of Draynor Village");
        player.getPackets().sendIComponentText(735, 64, "          Other Realms: The Gorak Plane");
        player.getPackets().sendIComponentText(735, 76, "          Islands: Poison Waste south of Isafdar");
    }

    public static boolean confirmRingHash(Player player) {
        int[] locationArray = (int[]) player.getAttributes().get(Attribute.FAIRY_RING_LOCATION_ARRAY).get();
        if (locationArray == null)
            return false;
        StringBuilder string = new StringBuilder();
        int index = 0;
        for (int letterValue : locationArray)
            string.append(LETTERS[index++][letterValue]);
        return sendRingTeleport(player, string.toString().toUpperCase());
    }

    public static boolean sendRingTeleport(Player player, int hash) {
        int letter1 = hash / 16;
        hash -= letter1 * 16;
        int letter2 = hash / 4;
        hash -= letter2 * 4;
        int letter3 = hash;
        StringBuilder string = new StringBuilder();
        string.append(LETTERS[0][letter1]);
        string.append(LETTERS[1][letter2]);
        string.append(LETTERS[2][letter3]);
        return sendRingTeleport(player, string.toString().toUpperCase());
    }

    public static boolean sendRingTeleport(Player player, String hash) {
        Rings ring;
        try {
            ring = Rings.valueOf(hash);
        } catch (Throwable e) {
            ring = null;
        }
        if (ring == null || ring.getTile() == null || (ring == Rings.BKQ)) {
            sendTeleport(player, new WorldTile(FAIRY_SOURCE, 2));
            return false;
        }
        sendTeleport(player, ring.getTile());
        return true;
    }

    private static void resetRingHash(Player player) {
        player.getAttributes().get(Attribute.FAIRY_RING_LOCATION_ARRAY).set(new int[3]);
        for (int i = 0; i < 3; i++)
            player.getVarsManager().sendVarBit(2341 + i, 0);
    }

    private static void sendTeleport(final Player player, final WorldTile tile) {
        Magic.sendTeleportSpell(player, FIRST_ANIMATION, SECOND_ANIMATION, FIRST_GRAPHICS, SECOND_GRAPHICS, 0, 0, tile, 2, false, Magic.OBJECT_TELEPORT);
    }

    public static void handleDialButtons(final Player player, int componentId) {
        int[] locationArray = (int[]) player.getAttributes().get(Attribute.FAIRY_RING_LOCATION_ARRAY).get();
        if (locationArray == null) {
            player.getInterfaceManager().closeInterfaces();
            return;
        }
        if (player.getAttributes().get("location_changing") != null)
            return;
        int index = (componentId - 23) / 2;
        if (componentId % 2 == 0)
            locationArray[index]++;
        else
            locationArray[index]--;
        locationArray = getCorrectValues(locationArray);
        player.getAttributes().get(Attribute.FAIRY_RING_LOCATION_ARRAY).set(locationArray);
        for (int i = 0; i < 3; i++)
            player.getVarsManager().sendVarBit(2341 + i, locationArray[i] == 1 ? 3 : locationArray[i] == 3 ? 1 : locationArray[i]);
    }

    private static int[] getCorrectValues(int[] locationArray) {
        int loop = 0;
        for (int values : locationArray) {
            locationArray[loop++] = values & 0x3;
        }
        return locationArray;
    }

    public static enum Rings {
        AIQ(new WorldTile(2996, 3114, 0)),
        AJQ(new WorldTile(2735, 5221, 0)),
        AJR(new WorldTile(2780, 3613, 0)),
        AKQ(new WorldTile(2319, 3619, 0)),
        AKS(new WorldTile(2571, 2956, 0)),
        ALP(new WorldTile(2468, 4189, 0)),
        ALQ(new WorldTile(3597, 3495, 0)),
        ALS(new WorldTile(2644, 3495, 0)),
        BIQ(new WorldTile(3251, 3095, 0)),
        BJQ(new WorldTile(1737, 5342, 0)),
        BJR(new WorldTile(2650, 4730, 0)),
        BKP(new WorldTile(2385, 3035, 0)),
        BKR(new WorldTile(3469, 3431, 0)),
        BLP(new WorldTile(2347, 5126, 0)),
        BLR(new WorldTile(2740, 3351, 0)),
        CIP(new WorldTile(2513, 3884, 0)),
        CIQ(new WorldTile(2528, 3127, 0)),
        CJR(new WorldTile(2705, 3576, 0)),
        CKR(new WorldTile(2801, 3003, 0)),
        CKS(new WorldTile(3447, 3470, 0)),
        CLR(new WorldTile(2735, 2742, 0)),
        CLS(new WorldTile(2682, 3081, 0)),
        DIP(new WorldTile(3763, 2930, 0)),
        DIS(new WorldTile(3108, 3149, 0)),
        DJP(new WorldTile(2658, 3230, 0)),
        DJR(new WorldTile(2676, 3587, 0)),
        DKP(new WorldTile(2900, 3111, 0)),
        DKR(new WorldTile(3129, 3496, 0)),
        DKS(new WorldTile(2744, 3719, 0)),
        DLQ(new WorldTile(3423, 3016, 0)),
        DLS(new WorldTile(3501, 9821, 3)),
        AIR(new WorldTile(2700, 3247, 0)),
        AJS(new WorldTile(2500, 3896, 0)),
        ALR(new WorldTile(3059, 4875, 0)),
        BIR(new WorldTile(2455, 4396, 0)),
        BIS(new WorldTile(2635, 3266, 0)),
        BKQ(new WorldTile(3041, 4532, 0)),
        CKP(new WorldTile(2075, 4848, 0)),
        CLP(new WorldTile(3082, 3206, 0)),
        DIR(new WorldTile(3038, 5348, 0)),
        DLR(new WorldTile(2213, 3099, 0)),

        ;
        private WorldTile tile;

        private Rings(WorldTile tile) {
            this.tile = tile;
        }

        public WorldTile getTile() {
            return tile;
        }
    }
}

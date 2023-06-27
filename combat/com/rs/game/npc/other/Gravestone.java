package com.rs.game.npc.other;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.item.FloorItem;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.Utility;

import skills.Skills;

public class Gravestone extends NPC {

	private static final List<Gravestone> GRAVESTONES = new ArrayList<Gravestone>();

	private String username;
	private int ticks;
	private String inscription;
	private List<FloorItem> floorItems;//not supported yet
	private int gravestone;
	private boolean blessed;
	private int hintIcon;
	private WorldTile deathTile;
	
	public static final void sendInterface(Player player) {
		player.getInterfaceManager().sendInterface(652);
		player.getPackets().sendUnlockIComponentOptionSlots(652, 31, 0, 78, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(652, 34, 0, 13, 0, 1);
		player.getVarsManager().sendVar(1146, player.getDetails().getGravestone().get() | 262112);
	}
	
	public static final void sendBuySelection(Player player, int option) {
		if (player.getDetails().getGravestone().get() == option) {
			player.getPackets().sendGameMessage("You already have this gravestone.");
			return;
		}
		if (player.getInventory().getCoinsAmount() < PRICES[option]) {
			player.getPackets().sendGameMessage("You don't have enough coins to purchase this gravestone.");
			return;
		}
		if(player.getInventory().canRemove(995, PRICES[option])){
			player.getDetails().getGravestone().set(option);
			player.getInterfaceManager().closeInterfaces();
		}
	}
	
	public static final int[] PRICES = { 
		0, 50, 500, 5000, 50000, 50000, 50000, 50000, 50000, 50000, 50000, 50000, 500000, 500000
	};

	public Gravestone(Player player, WorldTile deathTile) {
		super(getNPCId(player.getDetails().getGravestone().get()), deathTile, null, true);
		gravestone = player.getDetails().getGravestone().get();
		setDirection(Utility.getFaceDirection(0, -1));
		setNextAnimation(new Animation(gravestone == 1 ? 7396 : 7394));
		username = player.getUsername();
		ticks = getMaximumTicks(gravestone);
		inscription = getInscription(player.getDisplayName(), gravestone);
		floorItems = new ArrayList<FloorItem>();
		this.deathTile = deathTile;
		synchronized (GRAVESTONES) {
			Gravestone oldStone = getGraveStoneByUsername(username);
			if (oldStone != null) {
				addLeftTime(false);
				oldStone.deregister();
			}
			GRAVESTONES.add(this);
		}
		player.getPackets().sendRunScript(2434, ticks);
		hintIcon = player.getHintIconsManager().addHintIcon(this, 0, -1, true);
		player.getPackets().sendGlobalConfig(623, deathTile.getTileHash());
		player.getPackets().sendGlobalConfig(624, 0);
		player.getPackets().sendGlobalString(53, "Your gravestone marker");
		player.getPackets().sendGameMessage("Your items have been dropped at your gravestone, where they'll remain until it crumbles. Look at the world map to help find your gravestone.");
		player.getPackets().sendGameMessage("It looks like it'll survive another " + (ticks / 100) + " minute" + ((ticks / 100) == 1 ? "" : "s") + ".");
	}
	
	public static final void login(Player player) {
		Gravestone stone = getGraveStoneByUsername(player.getUsername());
		if (stone == null)
			return;
		player.getPackets().sendRunScript(2434, stone.ticks);
		stone.hintIcon = player.getHintIconsManager().addHintIcon(stone, 0, -1, true);
		player.getPackets().sendGlobalConfig(623, stone.deathTile.getTileHash());
		player.getPackets().sendGlobalConfig(624, 0);
		player.getPackets().sendGlobalString(53, "Your gravestone marker");
		player.getPackets().sendGameMessage("Your items have been dropped at your gravestone, where they'll remain until it crumbles. Look at the world map to help find your gravestone.");
		player.getPackets().sendGameMessage("It looks like it'll survive another " + (stone.ticks / 100) + " minute" + ((stone.ticks / 100) == 1 ? "" : "s") + ".");

	}

	@Override
	public void processNPC() {
		ticks--;
		if (ticks == 0)
			decrementGrave(-1, "Your grave has collapsed!");
		else if (ticks == 50)
			decrementGrave(2, "Your grave is collapsing.");
		else if (ticks == 100)
			decrementGrave(1, "Your grave is about to collapse.");
	}
	
	@Override
	public void deregister() {
		synchronized (GRAVESTONES) {
			GRAVESTONES.remove(this);
		}
		Player player = getPlayer();
		if (player != null) {
			player.getPackets().sendRunScript(2434, 0);
			player.getHintIconsManager().removeHintIcon(hintIcon);
			player.getPackets().sendGlobalConfig(623, -1);

		}
		super.deregister();
	}

	public final void decrementGrave(int stage, String message) {
		Player player = getPlayer();
		if (player != null) {
			player.getPackets().sendGameMessage("<col=7E2217>" + message);
			player.getPackets().sendRunScript(2434, ticks);
		}
		if (stage == -1) {
			addLeftTime(true);
			deregister();
		} else
			transformIntoNPC(getNPCId(gravestone) + stage);
	}

	public final void sendGraveInscription(Player reader) {
		reader.getInterfaceManager().sendInterface(266);
		reader.getVarsManager().sendVarBit(4191, gravestone == 0 ? 0 : 1);
		if (ticks <= 50)
			reader.getPackets().sendIComponentText(266, 23, "The inscription is too unclear to read.");
		else if (reader == getPlayer())
			reader.getPackets().sendIComponentText(266, 23, "It looks like it'll survive another " + ((ticks / 100) + 1) + " minutes. Isn't there something a bit odd about reading your own gravestone?");
		else
			reader.getPackets().sendIComponentText(266, 23, inscription);
	}

	public final void repair(Player blesser, boolean bless) {
		if (blesser.getSkills().getLevel(Skills.PRAYER) < (bless ? 70 : 2)) {
			blesser.getPackets().sendGameMessage("You need " + (bless ? 70 : 2) + " prayer to " + (bless ? "bless" : "repair") + " a gravestone.");
			return;
		}
		if (blesser.getUsername().equals(username)) {
			blesser.getPackets().sendGameMessage("The gods don't seem to approve of people attempting to " + (bless ? "bless" : "repair") + " their own gravestones.");
			return;
		}
		if (bless && blessed) {
			blesser.getPackets().sendGameMessage("This gravestone has already been blessed.");
			return;
		} else if (!bless && ticks > 100) {
			blesser.getPackets().sendGameMessage("This gravestone doesn't seem to need repair.");
			return;
		}
		ticks += bless ? 6000 : 500;
		blessed = true;
		decrementGrave(0, blesser.getDisplayName() + "has " + (bless ? "blessed" : "repaired") + " your gravestone. It should survive another " + (ticks / 100) + " minutes.");
		blesser.getPackets().sendGameMessage("You " + (bless ? "bless" : "repair") + " the grave.");
		blesser.getMovement().lock(2);
		blesser.setNextAnimation(new Animation(645));
	}

	public final void demolish(Player demolisher) {
		if (!demolisher.getUsername().equals(username)) {
			demolisher.getPackets().sendGameMessage("It would be impolite to demolish someone else's gravestone.");
			return;
		}
		addLeftTime(true);
		deregister();
		demolisher.getPackets().sendGameMessage("It looks like it'll survive another " + (ticks / 100) + " minutes. You demolish it anyway.");
	}

	public final void addLeftTime(boolean clean) {
		if (clean) {
			for (FloorItem item : floorItems)
				FloorItem.turnPublic(item, 60);
		} else {
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						for (FloorItem item : floorItems)
							FloorItem.turnPublic(item, 60);
					} catch (Throwable e) {
					}
				}
			}, (long) (ticks * 0.6), TimeUnit.SECONDS);
		}
	}

	public Player getPlayer() {
		return World.getPlayer(username).get();
	}

	public static final int getMaximumTicks(int gravestone) {
		switch (gravestone) {
		case 0:
			return 175;
		case 1:
		case 2:
			return 200;
		case 3:
			return 225;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
			return 250;
		case 12:
			return 275;
		case 13:
			return 300;
		}
		return 300;
	}

	public static final Gravestone getGraveStoneByUsername(String username) {
		for (Gravestone stone : GRAVESTONES)
			if (stone.username.equals(username))
				return stone;
		return null;
	}

	public static final int getNPCId(int gravestone) {
		if (gravestone == 13)
			return 13296;
		return 6565 + (gravestone * 3);
	}

	private static final String getInscription(String username, int gravestone) {
		username = Utility.formatPlayerNameForDisplay(username);
		switch (gravestone) {
		case 0:
		case 1:
			return "In memory of <i>" + username + "</i>,<br>who died here.";
		case 2:
		case 3:
			return "In loving memory of our dear friend <i>" + username + "</i>,who <br>died in this place @X@ minutes ago.";
		case 4:
		case 5:
			return "In your travels, pause awhile to remember <i>" + username + "</i>,<br>who passed away at this spot.";
		case 6:
			return "<i>" + username + "</i>, <br>an enlightened servant of Saradomin,<br>perished in this place.";
		case 7:
			return "<i>" + username + "</i>, <br>a most bloodthirsty follower of Zamorak,<br>perished in this place.";
		case 8:
			return "<i>" + username + "</i>, <br>who walked with the Balance of Guthix,<br>perished in this place.";
		case 9:
			return "<i>" + username + "</i>, <br>a vicious warrior dedicated to Bandos,<br>perished in this place.";
		case 10:
			return "<i>" + username + "</i>, <br>a follower of the Law of Armadyl,<br>perished in this place.";
		case 11:
			return "<i>" + username + "</i>, <br>servant of the Unknown Power,<br>perished in this place.";
		case 12:
			return "Ye frail mortals who gaze upon this sight, forget not<br>the fate of <i>" + username + "</i>, once mighty, now surrendered to the inescapable grasp of destiny.<br><i>Requiescat in pace.</i>";
		case 13:
			return "Here lies <i>" + username + "</i>, friend of dwarves. Great in<br>life, glorious in death. His/Her name lives on in<br>song and story.";
		default:
			return null;
		}
	}

}
